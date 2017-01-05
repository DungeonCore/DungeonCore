package lbn.item.itemInterface;

import lbn.player.status.IStatusManager;

import org.bukkit.entity.Player;

public interface AvailableLevelItemable {
	int getAvailableLevel();

	boolean isAvilable(Player player);

	IStatusManager getManager();;
}
