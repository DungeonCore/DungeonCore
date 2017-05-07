package net.l_bulb.dungeoncore.mob.customMob;

import net.l_bulb.dungeoncore.player.ItemType;

public interface SummonMobable {
  public int getDeadlineTick();

  public ItemType getUseItemType();
}
