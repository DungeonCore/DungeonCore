package net.l_bulb.dungeoncore.item.slot.magicstone;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent_old;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;

public abstract class CombatSlot extends AbstractSlot {
  abstract public void onCombat(PlayerCombatEntityEvent_old e);
}
