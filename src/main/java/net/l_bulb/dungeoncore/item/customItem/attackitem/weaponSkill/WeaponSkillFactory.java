package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.util.DungeonLogger;

public class WeaponSkillFactory {
  private static HashMap<String, WeaponSkillInterface> skillIDMap = new HashMap<>();

  private static Set<WeaponSkillInterface> skillList = new HashSet<>();

  private static HashMap<String, WeaponSkillSet> skillSetIdMap = new HashMap<>();

  /**
   * SkillListを取得
   *
   * @return
   */
  public static Collection<WeaponSkillInterface> getSortedSkillList() {
    return skillList;
  }

  /**
   * 武器スキルを登録する
   *
   * @param weaponSkill
   */
  public static void registData(WeaponSkillData data) {
    SpreadSheetWeaponSkill weaponSkill = tempInstanceMap.get(data.getId());
    if (weaponSkill == null) {
      DungeonLogger.development("武器スキル：" + data.getName() + "がプログラム内に存在しません");
      return;
    }
    weaponSkill.setData(data);
    regist(weaponSkill, data.getSkillSetId(), data.getSkillSetName());
  }

  /**
   * 武器スキルを登録
   *
   * @param weaponSkill
   */
  public static void regist(WeaponSkillInterface weaponSkill, String skillSetId, String skillSetName) {
    skillIDMap.put(weaponSkill.getId(), weaponSkill);
    skillList.add(weaponSkill);

    // スキルセットを登録
    if (skillSetId != null) {
      WeaponSkillSet skillSet = Optional.ofNullable(skillSetIdMap.get(skillSetId)).orElse(new WeaponSkillSet(skillSetId, skillSetName));
      skillSet.addSkill(weaponSkill);
      skillSetIdMap.put(skillSetId, skillSet);
    }

    if (weaponSkill instanceof ProjectileInterface) {
      ProjectileManager.regist((ProjectileInterface) weaponSkill);
    }
  }

  /**
   * 武器スキルを取得する
   *
   * @param id 武器スキルID
   */
  public static WeaponSkillInterface getWeaponSkill(String id) {
    return skillIDMap.get(id);
  }

  /**
   * 武器スキルセットを取得する
   *
   * @param id
   * @return
   */
  public static WeaponSkillSet getWeaponSkillSet(String id) {
    return skillSetIdMap.get(id);
  }

  // 一時的なインスンタンスを保持するためのクラス
  static HashMap<String, SpreadSheetWeaponSkill> tempInstanceMap = new HashMap<>();

  public static void registTempData(SpreadSheetWeaponSkill skill) {
    tempInstanceMap.put(skill.getId(), skill);
  }

}