package lbn.item.slot.slot;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.slot.AbstractSlot;


public  abstract class CombatSlot extends AbstractSlot{
	abstract public void onCombat(PlayerCombatEntityEvent e);
}
