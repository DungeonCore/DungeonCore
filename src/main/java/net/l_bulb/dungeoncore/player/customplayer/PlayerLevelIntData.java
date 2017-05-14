package net.l_bulb.dungeoncore.player.customplayer;

import java.io.Serializable;

import net.l_bulb.dungeoncore.api.LevelType;

public class PlayerLevelIntData implements Serializable {
  private static final long serialVersionUID = -8739766999079900117L;

  public PlayerLevelIntData(int swordData, int bowData, int magicData) {
    this.swordData = swordData;
    this.bowData = bowData;
    this.magicData = magicData;
    updateMainLevel();
  }

  /**
   * メインレベルを更新する
   */
  protected void updateMainLevel() {
    this.mainData = (int) ((swordData + bowData + magicData) / 3.0);
  }

  int swordData = 0;
  int bowData = 0;
  int magicData = 0;

  int mainData = 0;

  public int get(LevelType type) {
    switch (type) {
      case SWORD:
        return swordData;
      case BOW:
        return bowData;
      case MAGIC:
        return magicData;
      case MAIN:
        return mainData;
      default:
        throw new RuntimeException("unsupport level type:" + type);
    }
  }

  public void put(LevelType type, int data) {
    switch (type) {
      case SWORD:
        swordData = data;
        break;
      case BOW:
        bowData = data;
        break;
      case MAGIC:
        magicData = data;
        break;
      case MAIN:
        // 全てのDataにセットする
        swordData = data;
        bowData = data;
        magicData = data;
        break;
      default:
        throw new RuntimeException("unsupport level type:" + type);
    }
    // メインレベルを更新
    updateMainLevel();
  }

  public void add(LevelType type, int data) {
    switch (type) {
      case SWORD:
        swordData += data;
        break;
      case BOW:
        bowData += data;
        break;
      case MAGIC:
        magicData += data;
        break;
      case MAIN:
        // 全てのDataに加算する
        swordData += data;
        bowData += data;
        magicData += data;
        break;
      default:
        throw new RuntimeException("unsupport level type:" + type);
    }

    // メインレベルを更新する
    updateMainLevel();
  }

}
