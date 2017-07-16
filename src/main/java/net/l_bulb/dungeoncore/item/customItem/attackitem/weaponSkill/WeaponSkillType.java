package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import lombok.Getter;

public enum WeaponSkillType {
  SPECIAL_SKILL("スペシャルスキル"), NORMAL_SKILL("ノーマルスキル"), PASSIVE_SKILL("パッシブスキル");

  @Getter
  private String jpName;

  private WeaponSkillType(String jpName) {
    this.jpName = jpName;
  }

  /**
   * 日本語名からスキルタイプを取得する
   * 
   * @param jpName
   * @return
   */
  public static WeaponSkillType getWeaponSkillType(String jpName) {
    for (WeaponSkillType type : values()) {
      if (type.getJpName().equals(jpName)) { return type; }
    }
    return null;
  }
}
