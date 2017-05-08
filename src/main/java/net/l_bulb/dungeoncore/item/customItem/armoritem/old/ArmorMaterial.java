package net.l_bulb.dungeoncore.item.customItem.armoritem.old;

public enum ArmorMaterial {
  LEATHER(0.06, 0.04, 0.04, 0.08), GOLD(0.07, 0.06, 0.04, 0.1), CHAINMAIL(0.08, 0.08, 0.04, 0.124), IRON(0.09, 0.1, 0.04, 0.159), DIAMOND(0.1, 0.12,
      0.04, 0.212);

  /**
   * @param minDamageCut ダメージカットの最小値
   * @param minBossDamageCut ボスに対するダメージカット最小値
   * @param maxDamageCut ダメージカット最大値
   * @param maxBossDamageCut ボスに対するダメージカット最大値
   */
  private ArmorMaterial(double minDamageCut, double minBossDamageCut,
      double maxDamageCut, double maxBossDamageCut) {
    this.minDamageCut = minDamageCut;
    this.minBossDamageCut = minBossDamageCut;
    this.maxDamageCut = maxDamageCut;
    this.maxBossDamageCut = maxBossDamageCut;
  }

  double minDamageCut;
  double minBossDamageCut;
  double maxDamageCut;
  double maxBossDamageCut;

  /**
   * 強化してない状態でザコ敵に関してダメージをカットする倍率
   * 
   * @return
   */
  public double getBaseDamageCut() {
    return minDamageCut;
  }

  /**
   * 強化してない状態でボスに関してダメージをカットする倍率
   * 
   * @return
   */
  public double getBaseBossDamageCut() {
    return minBossDamageCut;
  }

  /**
   * 最大強化の時、追加でザコ敵に関してダメージをカットする割合
   * 
   * @return
   */
  public double getStrengthTotalDamageCut() {
    return maxDamageCut;
  }

  /**
   * 最大強化の時、追加でボスに関してダメージをカットする割合
   * 
   * @return
   */
  public double getStrengthBossTotalDamageCut() {
    return maxBossDamageCut;
  }
}
