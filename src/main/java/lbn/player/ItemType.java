package lbn.player;

import org.bukkit.Material;

import lbn.api.LevelType;
import lbn.util.ItemStackUtil;

public enum ItemType {
  SWORD(LevelType.SWORD, ItemStackUtil.getVanillaDamage(Material.WOOD_SWORD)), BOW(LevelType.BOW,
      ItemStackUtil.getVanillaDamage(Material.BOW)), MAGIC(LevelType.MAGIC, 6), OTHER(), IGNORE();

  private ItemType(LevelType type, double level0MinDamage) {
    this.levelType = type;
    this.level0MinDamage = level0MinDamage;
  }

  private ItemType() {
    this.levelType = LevelType.MAIN;
  }

  double level0MinDamage = 4;

  LevelType levelType;

  public LevelType getLevelType() {
    return levelType;
  }

  /**
   * レベルに応じた武器の最小ダメージ
   * 
   * @param level
   * @return
   */
  public double getMinDamage(int level) {
    return getMaxDamage(level) + getDecreaseDamage(level);
  }

  private double getDecreaseDamage(int level) {
    if (level <= 15) { return -4; }

    if (level <= 60) { return -level / 10.0 * 2.0 - 4; }

    return -16;
  }

  /**
   * レベルに応じた武器の最大ダメージを取得
   * 
   * @param availableLevel 利用可能レベル
   * @return
   */
  public double getMaxDamage(int availableLevel) {
    // キャッシュをするほうが遅くなるのでこのまま計算する
    if (availableLevel <= 60) {
      if (this == SWORD) {
        return 9 + Math.pow(availableLevel / 10.0, 2) * 2.6;
      } else if (this == BOW) {
        return 12 + Math.pow(availableLevel / 10.0, 2.1) * 2.7;
      } else if (this == MAGIC) { return 12 + Math.pow(availableLevel / 10.0, 2) * 2.6; }
    } else {
      if (this == SWORD) {
        return 9 + availableLevel / 10.0 * 6.6 + 5;
      } else if (this == BOW) {
        return 13 + Math.pow((availableLevel - 60) / 10.0, 1.1) * 6.7 + 5;
      } else if (this == MAGIC) { return 9 + availableLevel / 10.0 * 6.6 + 5; }
    }

    // その他の時は起こり得ないが剣にする
    return 12 + Math.pow(availableLevel / 10.0, 2) * 2.6;
  }
}
