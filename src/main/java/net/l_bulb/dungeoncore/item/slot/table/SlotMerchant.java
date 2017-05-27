package net.l_bulb.dungeoncore.item.slot.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.trade.TheLowMerchant;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchantRecipe;
import net.l_bulb.dungeoncore.common.trade.TheLowTrades;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

import net.md_5.bungee.api.ChatColor;

public class SlotMerchant extends TheLowMerchant {

  public SlotMerchant(Player p) {
    super(p);
  }

  public void open() {
    TheLowTrades.open(this, p);
  }

  @Override
  protected void onSetItem(InventoryView inv) {
    Inventory topInventory = inv.getTopInventory();
    // 武器
    ItemStack attackItem = topInventory.getItem(0);
    // 魔法石
    ItemStack magicStone = topInventory.getItem(1);

    // Slotを操作するためのクラスを作成する
    SlotSetOperator slotSetOperator = createSlotSetOperator(attackItem, magicStone);
    if (slotSetOperator == null) {
      sendRecipeList(INIT_RECIPE);
      p.updateInventory();
      return;
    }

    // 装着できるか確認する
    String check = slotSetOperator.check();

    // 装着結果の武器
    ItemStack attackResultItem = attackItem.clone();
    attackItem.setAmount(1);
    // 装着結果の武器
    ItemStack magicStoneResultItem = magicStone.clone();
    magicStoneResultItem.setAmount(1);

    if (check != null) {
      sendRecipeList(new TheLowMerchantRecipe(attackResultItem, magicStoneResultItem, ItemStackUtil.getItem(ChatColor.RED + "装着できません",
          Material.BARRIER, check)));
      p.updateInventory();
      return;
    }

    // スロットに魔法石をセットする
    slotSetOperator.setSlot();

    sendRecipeList(Arrays.asList(new TheLowMerchantRecipe(attackResultItem, magicStoneResultItem, getResultItem(slotSetOperator)),
        new TheLowMerchantRecipe(attackResultItem, magicStoneResultItem, slotSetOperator.getAttackItem().getItem())));
    p.updateInventory();
  }

  /**
   * SlotOperatorを生成する
   *
   * @param attackItem
   * @param magicStone
   * @return もし魔法石を装着できるアイテムでないならnullを返す
   */
  public SlotSetOperator createSlotSetOperator(ItemStack attackItem, ItemStack magicStone) {
    // カスタムアイテムに変換する
    ItemInterface customAttackItem = ItemManager.getCustomItem(attackItem);
    ItemInterface customMagicItem = ItemManager.getCustomItem(magicStone);

    // もしアイテムが不足しているなら何もしない
    if (customAttackItem == null || customMagicItem == null) { return null; }

    // もしアイテムが不足しているなら何もしない
    if (ItemManager.isImplemental(CombatItemable.class, customAttackItem) && customMagicItem instanceof SlotInterface) {
      SlotSetOperator slotSetOperator = new SlotSetOperator(((CombatItemable) customAttackItem).getCombatAttackItemStack(attackItem.clone()),
          (SlotInterface) customMagicItem);
      return slotSetOperator;
    } else {
      return null;
    }

  }

  /**
   * 魔法石を装着する時の表示アイテムを取得する
   *
   * @return
   */
  private ItemStack getResultItem(SlotSetOperator slotSetOperator) {
    ItemStack item = ItemStackUtil.getItem("魔法石装着", Material.CHEST);

    // Loreを生成
    ItemLoreToken loreToken = ItemLoreData.createLoreTokenInstance("ステータス");
    loreToken.addLore("装着できます");
    loreToken.addLore(slotSetOperator.getSuccessRate() + "%の確率で成功");

    // Loreをセットする
    ItemStackUtil.setLore(item, loreToken.getLoreWithTitle());

    return item;
  }

  @Override
  public String getName() {
    return "魔法石装着";
  }

  @Override
  public TheLowMerchantRecipe getShowResult(TheLowMerchantRecipe recipe) {
    this.slotSetOperator = null;

    // バリアブロック または 初期アイテムなら無視する
    if (ItemStackUtil.getMaterial(recipe.getResult()) == Material.BARRIER || INIT_RESULT_ITEM.equals(recipe.getResult())) { return null; }

    // 表示できるアイテムでないなら何もしない
    SlotSetOperator slotSetOperator = createSlotSetOperator(recipe.getBuy1(), recipe.getBuy2());
    if (slotSetOperator == null) { return null; }

    // 起こり得ないと思うがエラーがあるなら装着しない
    if (slotSetOperator.check() != null) {
      p.sendMessage("エラーが発生しました。(1)");
      return null;
    }

    // スロットを作成する
    slotSetOperator.setSlot();

    // 装着後のアイテム
    ItemStack newResultItem = null;

    // 成功
    if (p.getGameMode() == GameMode.CREATIVE || JavaUtil.isRandomTrue(slotSetOperator.getSuccessRate())) {
      newResultItem = slotSetOperator.getAttackItem().getItem();
      success = true;
    } else {
      // 失敗
      slotSetOperator.rollback();
      newResultItem = slotSetOperator.getAttackItem().getItem();
      success = false;
    }

    this.slotSetOperator = slotSetOperator;

    // 装着後のアイテムを取得
    recipe.setResult(newResultItem);
    return recipe;
  }

  boolean success = false;

  SlotSetOperator slotSetOperator = null;

  // 最初に表示するレシピ
  static final List<TheLowMerchantRecipe> INIT_RECIPE = new ArrayList<>();
  static final ItemStack INIT_ITEM1 = ItemStackUtil.getItem(ChatColor.WHITE + "武器", Material.IRON_SWORD, ChatColor.GREEN + "魔法石をつける武器");
  static final ItemStack INIT_ITEM2 = ItemStackUtil.getItem(ChatColor.WHITE + "魔法石", Material.INK_SACK, (byte) 1, ChatColor.GREEN + "装着する魔法石");
  static final ItemStack INIT_RESULT_ITEM = ItemStackUtil.getItem(ChatColor.WHITE + "魔法石装着後の武器", Material.DIAMOND_SWORD, ChatColor.GREEN
      + "魔法石を装着した武器");
  static {
    INIT_RECIPE.add(new TheLowMerchantRecipe(INIT_ITEM1, INIT_ITEM2, INIT_RESULT_ITEM));
  }

  @Override
  public List<TheLowMerchantRecipe> getInitRecipes() {
    return INIT_RECIPE;
  }

  @Override
  public void onFinishTrade(TheLowMerchantRecipe recipe) {
    if (slotSetOperator == null) { return; }

    if (success) {
      // コメントを表示
      p.sendMessage(ChatColor.GREEN + slotSetOperator.getScuessComment());
      // 音を再生
      p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
    } else {
      // コメントを表示
      p.sendMessage(ChatColor.RED + slotSetOperator.getFailureComment());
      // 音を再生
      p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
    }

    // クリエなら絶対成功にする
    if (p.getGameMode() == GameMode.CREATIVE) {
      p.sendMessage(ChatColor.YELLOW + "クリエイティブモードなので絶対成功します");
    }
  }

}
