package net.l_bulb.dungeoncore.item.system.craft.craftingViewer;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.PlayerCraftCustomItemEvent;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchant;
import net.l_bulb.dungeoncore.common.trade.TheLowMerchantRecipe;
import net.l_bulb.dungeoncore.common.trade.TheLowTrades;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeInterface;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeWithMainItem;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class CraftViewerForMainItemRecipe extends TheLowMerchant {

  private TheLowCraftRecipeInterface craftRecipe;
  private ItemInterface mainItem;
  private TheLowPlayer thelowPlayer;

  public CraftViewerForMainItemRecipe(TheLowPlayer thelowPlayer, TheLowCraftRecipeWithMainItem craftRecipe) {
    super(thelowPlayer.getOnlinePlayer());
    this.thelowPlayer = thelowPlayer;
    this.craftRecipe = craftRecipe;
    mainItem = craftRecipe.getMainItem();
  }

  @Override
  protected void onSetItem(InventoryView inv) {
    Inventory topInventory = inv.getTopInventory();
    ItemStack baseItem = topInventory.getItem(0);
    // アイテムが置かれてないなら初期画面を表示
    if (ItemStackUtil.isEmpty(baseItem)) {
      sendRecipeList(getInitRecipes());
      return;
      // 別のアイテムを置いてあったらバリアブロックを置く
    } else if (!mainItem.isThisItem(baseItem)) {
      sendRecipeList(Arrays.asList(new TheLowMerchantRecipe(mainItem.getItem(), ItemStackUtil.getItem("アイテムが違います", Material.BARRIER))));
      return;
    }

    ItemStack craftedItem = getCraftedItem(baseItem);

    // レシピとして表示するアイテム
    ItemStack dispItem = baseItem.clone();
    dispItem.setAmount(1);
    sendRecipeList(Arrays.asList(new TheLowMerchantRecipe(dispItem, craftedItem)));
  }

  /**
   * クラフト後のアイテムを取得する
   *
   * @param baseItem 元となるアイテム
   * @return
   */
  private ItemStack getCraftedItem(ItemStack baseItem) {
    int level = StrengthOperator.getLevel(baseItem);

    // 今はレベルだけ引き継ぐ
    ItemStack item = craftRecipe.getCraftItem().getItem();
    StrengthOperator.updateLore(item, level);
    return item;
  }

  @Override
  public String getName() {
    return "アイテム制作2";
  }

  @Override
  public TheLowMerchantRecipe getShowResult(TheLowMerchantRecipe recipe) {
    // 素材を全て持っていないなら取引できない
    if (!craftRecipe.hasAllMaterial(p, false)) {
      p.sendMessage("アイテムが足りないため取引出来ません");
      return null;
    }

    if (recipe.getResult() != null && recipe.getResult().getType() == Material.BARRIER) { return null; }
    p.updateInventory();
    return recipe;
  }

  @Override
  public List<TheLowMerchantRecipe> getInitRecipes() {
    return Arrays.asList(new TheLowMerchantRecipe(mainItem.getItem(), craftRecipe.getCraftItem().getItem()));
  }

  @Override
  public void onFinishTrade(TheLowMerchantRecipe recipe) {
    craftRecipe.removeMaterial(p.getInventory());

    // 取引欄に0個のアイテムが残るので2tick後に実行する
    new BukkitRunnable() {
      @Override
      public void run() {
        p.updateInventory();
      }
    }.runTaskLater(Main.plugin, 2);

    new PlayerCraftCustomItemEvent(thelowPlayer, craftRecipe, recipe.getResult()).callEvent();
  }

  public static void open(Player p, TheLowCraftRecipeWithMainItem recipe) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return;
    }

    if (recipe.isValidRecipe()) {
      CraftViewerForMainItemRecipe craftViewerForMainItemRecipe = new CraftViewerForMainItemRecipe(theLowPlayer, recipe);
      TheLowTrades.open(craftViewerForMainItemRecipe, p);
    }

  }

}
