package lbn.player.magicstoneOre;

import java.util.Random;

import org.bukkit.Material;

/**
 * 魔法鉱石の種類を管理するためのクラス
 * 
 * @author KENSUKE
 *
 */
public enum MagicStoneOreType {
  DIAOMOD_ORE("ダイヤ鉱石", Material.DIAMOND_ORE, 60 * 60 * 1, 60 * 60 * 3, 10), REDSTONE_ORE("レッドストーン鉱石", Material.REDSTONE_ORE, 60 * 40,
      (long) (60 * 60 * 1.5), 75), GOLD_ORE("金鉱石", Material.GOLD_ORE, 20 * 30, (long) (20 * 60 * 1.5), 50), EMERALD_ORE("エメラルド鉱石",
          Material.EMERALD_ORE, 60 * 30, (long) (60 * 60 * 1.5), 0), IRON_ORE("鉄鉱石", Material.IRON_ORE, 60 * 10, 60 * 30,
              30), COAL_ORE("石炭鉱石", Material.COAL_ORE, 60 * 10, 60 * 30, 10), LAPIS_ORE("ラピス鉱石", Material.LAPIS_ORE, 60 * 10, 60 * 30, 15);

  // 日本語名
  String jpName;

  // 鉱石のブロックの素材
  Material m;

  // 復活する最大時間
  long maxMinTick;

  // 復活する最小時間
  long minMinTick;

  // 掘ったときに取得する経験値
  int exp;

  private MagicStoneOreType(String jpName, Material m, long minMinSec, long maxMinSec, int exp) {
    this.jpName = jpName;
    this.m = m;
    this.maxMinTick = maxMinSec * 20;
    this.minMinTick = minMinSec * 20;
    this.exp = exp;
  }

  /**
   * 掘ったときに取得する経験値
   * 
   * @return
   */
  public int getExp() {
    return exp;
  }

  /**
   * ブロックの素材を取得する
   * 
   * @return
   */
  public Material getMaterial() {
    return m;
  }

  /**
   * 日本語名を取得
   * 
   * @return
   */
  public String getJpName() {
    return jpName;
  }

  /**
   * 鉱石の最大復活時間を取得
   * 
   * @return
   */
  public long getMaxRelocationTick() {
    return maxMinTick;
  }

  /**
   * 鉱石の最小復活時間を取得
   * 
   * @return
   */
  public long getMinRelocationTick() {
    return minMinTick;
  }

  static Random random = new Random();

  /**
   * 再配置される時間をランダムに取得
   * 
   * @return
   */
  public long getRelocationTick() {
    // return random.nextInt((int) (getMaxRelocationTick() - getMinRelocationTick())) + getMinRelocationTick();
    return 3 * 20;
  }

  /**
   * マテリアルから魔法鉱石を取得。もし存在しない日本語名の時はnullを返す
   * 
   * @param m
   * @return
   */
  public static MagicStoneOreType FromMaterial(Material m) {
    for (MagicStoneOreType type : values()) {
      if (type.getMaterial().equals(m)) { return type; }
    }

    if (m == Material.GLOWING_REDSTONE_ORE) { return REDSTONE_ORE; }
    return null;
  }

  /**
   * 日本語名から魔法鉱石を取得する。もし存在しない日本語名の時はnullを返す
   * 
   * @param jpName
   * @return
   */
  public static MagicStoneOreType FromJpName(String jpName) {
    if (jpName == null) { return null; }

    for (MagicStoneOreType type : values()) {
      // 日本語名が同じならそれを返す
      if (type.getJpName().equals(jpName)) { return type; }
    }
    return null;
  }
}
