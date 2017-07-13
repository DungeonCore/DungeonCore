package net.l_bulb.dungeoncore.item.statusItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum SetItemPartsType {
  HELMET(true, 39), CHEST_PLATE(true, 38), LEGGINSE(true, 37), BOOTS(true, 36);
  // , SLOT1(false, 9), SLOT2(false, 17),

  private SetItemPartsType(boolean isEquip, int slot) {
    this.isEquip = isEquip;
    this.slot = slot;
  }

  int slot;

  public int getSlot() {
    return slot;
  }

  boolean isEquip;

  public boolean isEquipParts() {
    return isEquip;
  }

  public ItemStack getItemStackByParts(Player p) {
    return p.getInventory().getItem(slot);
  }

  public static SetItemPartsType getTypeBySlot(int slot) {
    for (SetItemPartsType type : values()) {
      if (type.slot == slot) { return type; }
    }
    return null;
  }
}
