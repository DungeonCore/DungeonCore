package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.api.LevelType;

/**
 * レベル制限があるアイテムに設定する
 *
 */
public interface AvailableLevelItemable {
  int getAvailableLevel();

  LevelType getLevelType();
}
