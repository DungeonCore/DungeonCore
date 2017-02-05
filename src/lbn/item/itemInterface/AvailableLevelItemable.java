package lbn.item.itemInterface;

import lbn.api.LevelType;

import org.bukkit.entity.Player;

public interface AvailableLevelItemable {
	int getAvailableLevel();

	boolean isAvilable(Player player);

	LevelType getLevelType();;
}
