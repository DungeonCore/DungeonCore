package net.l_bulb.dungeoncore.api;

import java.util.Arrays;
import java.util.List;

public enum LevelType {
  SWORD("剣レベル"), BOW("弓レベル"), MAGIC("魔法レベル"), MAIN("メインレベル");

  String name;

  String weaponName;

  static private List<String> names = Arrays.asList(SWORD.getName(), BOW.getName(), MAGIC.getName(), MAIN.getName());

  private LevelType(String name) {
    this.name = name;
    this.weaponName = name.replace("レベル", "");
  }

  public String getName() {
    return name;
  }

  /**
   * 名称のリストを取得
   *
   * @return
   */
  public static List<String> getNames() {
    return names;
  }

  /**
   * 日本語表記からインスタンスを取得
   *
   * @param jpName
   * @return
   */
  public static LevelType fromJpName(String jpName) {
    for (LevelType type : values()) {
      if (type.name.equals(jpName)) { return type; }
    }
    return null;
  }

  /**
   * 対応する武器名を取得
   *
   * @return
   */
  public String getWeaponName() {
    return weaponName;
  }
}
