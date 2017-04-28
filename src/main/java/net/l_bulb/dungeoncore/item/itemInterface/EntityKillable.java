package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;

public interface EntityKillable extends ItemInterface {
  void onKillEvent(PlayerKillEntityEvent e);
}
