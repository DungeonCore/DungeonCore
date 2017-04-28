package net.l_bulb.dungeoncore.item.slot.slot;

import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;

public abstract class KillSlot extends AbstractSlot {
  abstract public void onKill(PlayerKillEntityEvent e);
}
