package main.item.slot.slot;

import main.common.event.player.PlayerRightShiftClickEvent;
import main.item.slot.AbstractSlot;


public abstract class ShiftRightClickSlot extends AbstractSlot{
	abstract public void onPlayerRightShiftClick(PlayerRightShiftClickEvent e);

}
