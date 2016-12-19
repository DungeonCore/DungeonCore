package lbn.item.slot.slot;

import lbn.common.event.player.PlayerRightShiftClickEvent;
import lbn.item.slot.AbstractSlot;


public abstract class ShiftRightClickSlot extends AbstractSlot{
	abstract public void onPlayerRightShiftClick(PlayerRightShiftClickEvent e);

}
