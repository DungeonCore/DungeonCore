package net.l_bulb.dungeoncore.item.slot;

import org.bukkit.ChatColor;

public interface SlotInterface {
  public String getSlotName();

  public String getSlotDetail();

  public String getId();

  public ChatColor getNameColor();

  public SlotType getSlotType();

  public SlotLevel getLevel();

  default public boolean isSame(SlotInterface slot) {
    if (slot != null) { return getId().equals(slot.getId()); }
    return false;
  }
}
