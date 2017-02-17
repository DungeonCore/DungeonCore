package lbn.item.itemInterface;

import lbn.api.LevelType;

/**
 * レベル制限があるアイテムに設定する
 *
 */
public interface AvailableLevelItemable {
	int getAvailableLevel();

	LevelType getLevelType();
}
