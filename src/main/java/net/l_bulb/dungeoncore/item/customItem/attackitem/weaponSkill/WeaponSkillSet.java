package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class WeaponSkillSet {
  private String weaponSkillSet;

  public WeaponSkillSet(String weaponSkillSet) {
    this.weaponSkillSet = weaponSkillSet;
  }

  @Setter
  private WeaponSkillInterface specialSkill;

  private List<WeaponSkillInterface> normalSkillList;

  /**
   * 通常スキルを追加する
   *
   * @param weaponSkill
   */
  public void addNormalSkill(WeaponSkillInterface weaponSkill) {
    normalSkillList.add(weaponSkill);
  }
}
