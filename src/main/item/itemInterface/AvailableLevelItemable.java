package main.item.itemInterface;

import main.player.status.IStatusManager;

import org.bukkit.entity.Player;

public interface AvailableLevelItemable {
	int getAvailableLevel();

	boolean isAvilable(Player player);

	IStatusManager getManager();;
}
