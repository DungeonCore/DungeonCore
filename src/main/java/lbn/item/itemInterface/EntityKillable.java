package lbn.item.itemInterface;

import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.item.ItemInterface;

public interface EntityKillable extends ItemInterface{
	void onKillEvent(PlayerKillEntityEvent e);
}
