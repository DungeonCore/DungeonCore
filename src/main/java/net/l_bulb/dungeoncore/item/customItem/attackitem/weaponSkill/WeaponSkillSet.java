package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.List;

import net.l_bulb.dungeoncore.util.TheLowValidates;

import lombok.Getter;

@Getter
public class WeaponSkillSet {
  private String weaponSkillSetName;

  private String skillSetId;

  public WeaponSkillSet(String skillSetId, String weaponSkillSetName) {
    this.skillSetId = skillSetId;
    this.weaponSkillSetName = weaponSkillSetName;
  }

  // スペシャルスキル
  private WeaponSkillInterface specialSkill;

  // ノーマルスキル
  private List<WeaponSkillInterface> normalSkillList;

  /**
   * 武器スキルを追加する
   *
   * @param weaponSkill
   */
  public void addSkill(WeaponSkillInterface weaponSkill) {
    switch (weaponSkill.geWeaponSkillType()) {
      case NORMAL_SKILL:
        normalSkillList.add(weaponSkill);
        break;
      case SPECIAL_SKILL:
        specialSkill = weaponSkill;
        break;
      default:
        TheLowValidates.throwIllegalState("不正なスキルタイプです：" + weaponSkill.geWeaponSkillType());
        break;
    }
  }
}
