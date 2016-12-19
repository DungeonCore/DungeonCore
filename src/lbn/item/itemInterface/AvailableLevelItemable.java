package lbn.item.itemInterface;

import org.bukkit.entity.Player;

import lbn.player.status.IStatusManager;

public interface AvailableLevelItemable {
	int getAvailableLevel();

	boolean isAvilable(Player player);

	IStatusManager getManager();;
}
