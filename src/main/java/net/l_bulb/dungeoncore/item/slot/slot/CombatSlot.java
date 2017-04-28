package net.l_bulb.dungeoncore.item.slot.slot;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;

public abstract class CombatSlot extends AbstractSlot {
  abstract public void onCombat(PlayerCombatEntityEvent e);
}
