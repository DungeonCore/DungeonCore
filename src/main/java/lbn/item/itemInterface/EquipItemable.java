package lbn.item.itemInterface;

import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;

public interface EquipItemable extends ItemInterface, AvailableLevelItemable {
  /**
   * アイテムの最大耐久度を取得
   * 
   * @param e
   * @return
   */
  public short getMaxDurability(ItemStack e);
}
