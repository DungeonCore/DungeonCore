package net.l_bulb.dungeoncore.player;

import net.l_bulb.dungeoncore.api.LevelType;

import lombok.Getter;

@Getter
public enum ItemType {
  SWORD(LevelType.SWORD, "剣"), BOW(LevelType.BOW, "弓"), MAGIC(LevelType.MAGIC, "魔法"), OTHER(LevelType.MAIN, "メイン"), IGNORE();

  private String jpName;
  LevelType levelType;

  private ItemType(LevelType type, String jpName) {
    this.levelType = type;
    this.jpName = jpName;
  }

  private ItemType() {
    this(LevelType.MAIN, "その他");
  }
}
