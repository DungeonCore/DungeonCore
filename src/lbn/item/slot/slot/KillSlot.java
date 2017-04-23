package lbn.item.slot.slot;

import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.item.slot.AbstractSlot;

public abstract class KillSlot extends AbstractSlot{
	abstract public void onKill(PlayerKillEntityEvent e);
}
