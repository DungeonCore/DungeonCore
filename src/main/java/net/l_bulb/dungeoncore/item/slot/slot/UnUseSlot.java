package net.l_bulb.dungeoncore.item.slot.slot;

import net.l_bulb.dungeoncore.item.slot.AbstractSlot;

public abstract class UnUseSlot extends AbstractSlot {
  @Override
  public boolean isShowItemList() {
    return false;
  }
}
