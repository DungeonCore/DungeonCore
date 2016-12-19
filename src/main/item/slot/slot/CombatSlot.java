package main.item.slot.slot;

import main.common.event.player.PlayerCombatEntityEvent;
import main.item.slot.AbstractSlot;


public  abstract class CombatSlot extends AbstractSlot{
	abstract public void onCombat(PlayerCombatEntityEvent e);
}
