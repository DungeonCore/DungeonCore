package main.item.itemInterface;

import main.common.event.player.PlayerCombatEntityEvent;
import main.item.ItemInterface;

public interface CombatItemable extends ItemInterface{
	void onCombatEntity(PlayerCombatEntityEvent e);
}
