package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface EquipItemable extends ItemInterface, AvailableLevelItemable {
  /**
   * アイテムの最大耐久度を取得
   * 
   * @param e
   * @return
   */
  public short getMaxDurability(ItemStack e);
}
