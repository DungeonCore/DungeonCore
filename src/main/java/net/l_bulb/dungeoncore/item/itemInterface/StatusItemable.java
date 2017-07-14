package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.statusItem.StatusItemData;

public interface StatusItemable extends ItemInterface {
  // /**
  // * アイテムの装備を解除または装備した時の処理
  // *
  // * @param p
  // * @param item
  // */
  // default void onEquip(Player p, ItemStack item) {
  // SetItemManager.updateAllSetItem(p, true);
  // }

  /**
   * ステータスアイテムを取得する
   *
   * @param item 装備しているアイテム
   *
   * @return
   */
  default StatusItemData getStatusItem(ItemStack item) {
    return new StatusItemData(item);
  }
}
