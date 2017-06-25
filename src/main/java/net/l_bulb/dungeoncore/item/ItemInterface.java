package net.l_bulb.dungeoncore.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.player.ItemType;

public interface ItemInterface {
  /**
   * アイテム名を取得する
   *
   * @return
   */
  String getItemName();

  /**
   * アイテム一覧に表示するならTRUE
   *
   * @return
   */
  boolean isShowItemList();

  default String getSimpleName() {
    return ChatColor.stripColor(getItemName());
  }

  /**
   * アイテムを取得
   *
   * @return
   */
  ItemStack getItem();

  /**
   * このアイテムを同値ならTRUE
   *
   * @param item
   * @return
   */
  boolean isThisItem(ItemStack item);

  /**
   * アイテムのタイプを取得
   *
   * @return
   */
  ItemType getAttackType();

  /**
   * クエストアイテムならTRUE
   *
   * @return
   */
  boolean isQuestItem();

  /**
   * idを取得
   *
   * @return
   */
  String getId();

  /**
   * 売値を取得
   *
   * @param item
   * @return
   */
  int getBuyPrice(ItemStack item);

  /**
   * 強化できるならTRUE
   *
   * @return
   */
  boolean isStrengthItem();

  /**
   * Playerが死んだ時にロストするならTRUE
   *
   * @return
   */
  default boolean isRemoveWhenDeath() {
    return false;
  }
}
