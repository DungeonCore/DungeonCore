package net.l_bulb.dungeoncore.item.system.strength;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.ChangeStrengthLevelItemEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class StrengthOperator {
  /**
   * 指定されたアイテムのレベルを取得
   *
   * @param strength
   * @param item
   * @return
   */
  public static int getLevel(ItemStack item) {
    ItemInterface customItem = ItemManager.getCustomItem(item);
    // 強化できないアイテムなら何もしない
    if (customItem == null || !(customItem instanceof Strengthenable)) { return 0; }
    Strengthenable strength = (Strengthenable) customItem;

    String dispName = ItemStackUtil.getName(item);
    String replace = dispName.replace(strength.getItemName(), "").replace(ChatColor.RESET.toString() + ChatColor.RED + " +", "").trim();

    if (!replace.isEmpty() && NumberUtils.isDigits(replace)) { return Integer.parseInt(replace); }
    return 0;
  }

  /**
   * 指定されたアイテムを指定されたレベルに強化する
   *
   * @param strength
   * @param item
   * @param toLevel
   */
  public static void updateLore(ItemStack item, int toLevel) {
    // もとのアイテム
    ItemStack clone = item.clone();

    ItemInterface customItem = ItemManager.getCustomItem(item);
    // 強化できないアイテムなら何もしない
    if (customItem == null || !customItem.isStrengthItem()) { return; }
    Strengthenable strength = (Strengthenable) customItem;

    if (toLevel < 0) {
      toLevel = 0;
    }
    if (toLevel > strength.getMaxStrengthCount()) {
      toLevel = strength.getMaxStrengthCount();
    }

    ItemLoreData itemLoreData = new ItemLoreData(item);

    itemLoreData.addLore(getStrengthLoreToken(strength, toLevel, new ItemStackNbttagAccessor(item)));

    ItemStackUtil.setLore(item, itemLoreData.getLore());

    // nameを変更する
    if (toLevel == 0) {
      ItemStackUtil.setDispName(item, strength.getItemName());
    } else {
      ItemStackUtil.setDispName(item, strength.getItemName() + ChatColor.RESET + ChatColor.RED + " +" + toLevel);
    }

    // TODO getAfterで更新させる
    ChangeStrengthLevelItemEvent event = new ChangeStrengthLevelItemEvent(clone, item, StrengthOperator.getLevel(clone), toLevel);
    Bukkit.getServer().getPluginManager().callEvent(event);
  }

  /**
   * 強化性能のLoreを取得
   *
   * @param strength
   * @param level
   * @return
   */
  public static ItemLoreToken getStrengthLoreToken(Strengthenable strength, int level, ItemStackNbttagAccessor setter) {
    ItemLoreToken itemLoreToken = new ItemLoreToken(ItemLoreToken.TITLE_STRENGTH);
    // Loreをセットする
    strength.setStrengthDetail(level, itemLoreToken, setter);

    if (itemLoreToken.size() == 0) {
      itemLoreToken.addLore("なし");
    }
    return itemLoreToken;
  }

  public static ItemStack getItem(ItemStack item, int level) {
    // 強化できるアイテムか確認
    ItemInterface itemInterface = ItemManager.getCustomItem(item);
    if (itemInterface == null) { return item; }

    if (itemInterface.isStrengthItem()) {
      updateLore(item, level);
    }
    return item;
  }

  /**
   * 強化できるアイテムならTRUE
   *
   * @param item
   * @return
   */
  public static boolean canStrength(ItemStack item) {
    ItemInterface customItem = ItemManager.getCustomItem(item);
    if (customItem == null) {
      return false;
    } else {
      return ItemManager.isImplemental(Strengthenable.class, customItem);
    }
  }
}
