package lbn.item.itemInterface;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.ItemInterface;

public interface CombatItemable extends ItemInterface{
	void onCombatEntity(PlayerCombatEntityEvent e);
}
