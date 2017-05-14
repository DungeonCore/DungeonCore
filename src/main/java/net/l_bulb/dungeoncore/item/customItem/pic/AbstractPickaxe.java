package net.l_bulb.dungeoncore.item.customItem.pic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerBreakMagicOreEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.MagicPickaxeable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.TitleSender;

public abstract class AbstractPickaxe extends AbstractItem implements MagicPickaxeable {

  int level = 1;

  public AbstractPickaxe(int level) {
    this.level = level;
  }

  /**
   * ピッケルのレベルを取得
   *
   * @return
   */
  public int getLevel() {
    return level;
  }

  private static final String MAGIC_PICKAXE_EXP = "magic_pickaxe_exp";

  /**
   * ピッケルの素材の日本語名
   *
   * @return
   */
  abstract String getMaterialName();

  @Override
  public String getItemName() {
    return getMaterialName() + "のピッケル  [レベル " + getLevel() + "]";
  }

  @Override
  public String getId() {
    return "magic_" + getMaterial().toString().toLowerCase() + "_" + getLevel();
  }

  Random random = new Random();

  @Override
  public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e) {
    if (!canDestory(e.getBrokenType())) {
      e.setCancelled(true);
      return;
    }
    ItemStack useItem = e.getUseItem();
    // 次のレベルを取得
    short nextExp = (short) (getPickExp(useItem) + e.getBrokenType().getExp());

    // ラピスの個数処理
    if (e.getBrokenType() == MagicStoneOreType.LAPIS_ORE) {
      // 個数をセットする
      if (getLapisCount(nextExp) > 1) {
        int nextInt = random.nextInt(getLapisCount(nextExp) - 1) + 1;
        e.getAcquisition().setAmount(nextInt);
      } else {
        e.getAcquisition().setAmount(1);
      }
    }

    // 最大レベル未満の時はレベルを追加する
    if (nextExp < getMaxExp()) {
      updatePickExp(useItem, nextExp);
    } else {
      // 次のピッケルがない場合は最大レベルまでしか追加しない
      if (getNextPickAxe() == null) {
        updatePickExp(useItem, getMaxExp());
        return;
      }
      // 進化後のピッケルをセットする
      ItemStack nextPickAxe = getNextPickAxe().getItem();
      // 余った経験値をセットする
      updatePickExp(nextPickAxe, (short) (nextExp - getMaxExp()));
      e.getPlayer().setItemInHand(nextPickAxe);
      // メッセージを送信する
      e.getPlayer().sendMessage(ChatColor.GREEN + "ピッケルが" + org.bukkit.ChatColor.GOLD + getNextPickAxe().getItemName() + ChatColor.GREEN + "に進化した。");

      // タイトルを表示
      TitleSender titleSender = new TitleSender();
      titleSender.setTitle("ピッケル レベルアップ", ChatColor.GREEN, false);
      titleSender.setSubTitle(getNextPickAxe().getItemName() + "に進化した", ChatColor.YELLOW, false);
      titleSender.execute(e.getPlayer());

      // 音をだす
      e.getLocation().getWorld().playSound(e.getLocation(), Sound.ORB_PICKUP, 1, (float) 0.1);
    }
  }

  abstract public int getLapisCount(short nextLevel);

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    // 0レベルにセットする
    updatePickExp(item, (short) 0);

    // 効率強化のレベルを取得
    int effecientLevel = level / 2;
    if (effecientLevel != 0) {
      // 効率強化のエンチャントをつける
      item.addEnchantment(Enchantment.DIG_SPEED, effecientLevel);
    }
    return item;
  }

  /**
   * 進化時のピッケルを取得する
   *
   * @return
   */
  abstract public AbstractPickaxe getNextPickAxe();

  /**
   * このアイテムの最大レベルを取得
   *
   * @return
   */
  abstract public short getMaxExp();

  abstract public boolean canDestory(MagicStoneOreType type);

  /**
   * ピッケルのレベルを取得
   *
   * @param item
   * @return
   */
  public static short getPickExp(ItemStack item) {
    short nbtTagShort = ItemStackUtil.getNBTTagShort(item, MAGIC_PICKAXE_EXP);
    return nbtTagShort;
  }

  /**
   * ピッケルのレベルを設定する
   *
   * @param item
   * @param level
   */
  public void updatePickExp(ItemStack item, short level) {
    // NBTTagを設定
    ItemStackUtil.setNBTTag(item, MAGIC_PICKAXE_EXP, level);

    List<String> lore = ItemStackUtil.getLore(item);
    Iterator<String> iterator = lore.iterator();
    // ピッケルレベルのLoreを削除する
    while (iterator.hasNext()) {
      String next = iterator.next();
      if (next.contains("ピッケルレベル ")) {
        iterator.remove();
      }
    }
    // レベルを記載
    lore.add(ChatColor.GREEN + "ピッケルレベル " + level + "/" + getMaxExp());

    ItemStackUtil.setLore(item, lore);
  }

  @Override
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = super.getStandardLoreToken();
    if (getNextPickAxe() != null) {
      loreToken.addLore(getMaxExp() + "レベルで" + getNextPickAxe().getItemName() + "に進化する");
    }
    if (getLapisCount((short) 0) != 1) {
      loreToken.addLore("一定確率でラピスを" + getLapisCount((short) 0) + "個まで取得");
    }
    return loreToken;
  }

  @Override
  protected ItemStack getItemStackBase() {
    return ItemStackUtil
        .getItemStackByCommand("/give @p minecraft:"
            + getGiveItemId()
            + " 1 0 {Unbreakable:1,HideFlags:8,CanDestroy:[\"minecraft:coal_ore\",\"minecraft:iron_ore\",\"minecraft:lapis_ore\",\"minecraft:redstone_ore\",\"minecraft:gold_ore\",\"minecraft:diamond_ore\", \"minecraft:lit_redstone_ore\"]}");
  }

  /**
   * Giveコマンドに使うIDを取得
   *
   * @return
   */
  abstract public String getGiveItemId();

  /**
   * 全てのレベルのピッケルを取得する
   *
   * @return
   */
  abstract public List<ItemInterface> getAllLevelPick();

  /**
   * 関係のあるアイテムを全て取得
   *
   * @return
   */
  public List<ItemInterface> getAllRelativeItem() {
    ArrayList<ItemInterface> itemList = new ArrayList<>(getAllLevelPick());
    itemList.add(new PickaxeSelector(this));
    return itemList;
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }

}
