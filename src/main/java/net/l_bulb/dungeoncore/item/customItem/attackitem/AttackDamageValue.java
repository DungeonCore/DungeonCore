package net.l_bulb.dungeoncore.item.customItem.attackitem;

import net.l_bulb.dungeoncore.player.ItemType;

public class AttackDamageValue {
  // public static double minDamage(ItemType type, int level) {
  // return 0;
  // }
  // public static double maxDamage(ItemType type, int level) {
  // return 0;
  // }

  /**
   * 与えられた戦闘負荷から攻撃力を取得する
   * 
   * @param combatLoad
   * @param availableLevel
   * @return
   */
  public static double getAttackDamageValue(double combatLoad, int availableLevel) {
    combatLoad = Math.max(combatLoad, 0);
    return levelMobHp[availableLevel] / combatLoad;
  }

  /**
   * 戦闘負荷を取得
   * 
   * @param weaponLevel
   * @param type
   * @return
   */
  public static double getCombatLoad(int weaponLevel, ItemType type) {
    weaponLevel = Math.max(weaponLevel, 1);
    weaponLevel = Math.min(weaponLevel, 20);

    if (type == ItemType.BOW) {
      return bowCombatLoad[weaponLevel - 1];
    } else {
      return swordCombatLoad[weaponLevel - 1];
    }
  }

  static double[] swordCombatLoad = {
      5.6,
      5.4,
      5.2,
      5,
      4.8,
      4.6,
      4.4,
      4.2,
      4,
      3.8,	// 10
      3.6,
      3.4,
      3.2,
      3,
      2.8,
      2.6,
      2.4,
      2.2,
      2,
      1.8
  };
  static double[] bowCombatLoad = {
      4,
      3.83,
      3.66,
      3.49,
      3.32,
      3.15,
      2.98,
      2.81,
      2.64,
      2.47,// 10
      2.3,
      2.13,
      1.96,
      1.79,
      1.62,
      1.45,
      1.28,
      1.11,
      0.94,
      0.77
  };

  static double[] levelMobHp = {
      29,
      29.624,
      30.02205596,
      30.67067165,
      31.51540094,
      32.53115129,
      33.70603634,
      35.03539115,
      36.51907542,
      38.16010729,
      39.96392066,
      41.9379476,
      44.09138513,
      46.43507456,
      48.9814541,
      51.74456269,
      54.74008178,
      57.98540731,
      61.49974731,
      65.30424251,
      69.42210857,
      73.87879972,
      78.70219403,
      83.92280117,
      89.57399384,
      95.69226467,
      102.3175104,
      109.4933456,
      117.267449,
      125.6919447,
      134.8238224,
      144.7253997,
      155.4648312,
      167.1166687,
      179.7624775,
      193.4915153,
      208.4014789,
      224.5993261,
      242.202181,
      261.3383307,
      282.1483224,
      304.7861728,
      329.4206993,
      356.2369881,
      385.4380106,
      417.246407,
      451.9064507,
      489.6862159,
      530.8799681,
      575.8108003,
      624.8335425,
      678.3379724,
      736.7523589,
      800.5473744,
      870.2404147,
      946.40037,
      1029.652895,
      1120.686232,
      1220.257643,
      1329.200525,
      1448.432268,
      1578.962949,
      1721.904944,
      1878.483557,
      2050.048788,
      2238.088342,
      2444.242034,
      2670.317728,
      2918.308991,
      3190.414629,
      3489.060335,
      3816.922654,
      4176.955551,
      4572.419827,
      5006.915741,
      5484.419154,
      6009.321606,
      6586.474753,
      7221.239646,
      7919.541395,
      8687.929803,
      9533.646652,
      10464.70037,
      11489.94891,
      12619.19174,
      13863.27198,
      15234.18988,
      16745.22874,
      18411.09489,
      20248.07316
  };
}
