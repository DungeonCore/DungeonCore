package main.item.slot.slot;

import main.common.event.player.PlayerKillEntityEvent;
import main.item.slot.AbstractSlot;

public abstract class KillSlot extends AbstractSlot{
	abstract public void onKill(PlayerKillEntityEvent e);
}
