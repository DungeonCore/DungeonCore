package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all.WeaponSkillBlastOff;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all.WeaponSkillBlastOffLevel2;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all.WeaponSkillCancel;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow.ArrowStorm;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow.BlindEye;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow.Finale;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow.IceArrow;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow.ReleaseAura;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic.Explosion;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic.GravityField;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic.HealRain;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic.LeafFlare;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.magic.MeteoStrike;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.BloodyHeal;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.BurstFlame;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.GrandSpike;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.LightningOrder;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.Lump;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.sword.ProtectionArmor;
import net.l_bulb.dungeoncore.util.DungeonLogger;

public class WeaponSkillFactory {
  private static HashMap<String, WeaponSkillInterface> skillMap = new HashMap<>();

  private static TreeSet<WeaponSkillInterface> skillLevelSkillMap = new TreeSet<>(new Comparator<WeaponSkillInterface>() {
    @Override
    public int compare(WeaponSkillInterface o1, WeaponSkillInterface o2) {
      // スキル解除は一番最初に表示する
      if (WeaponSkillCancel.isThisSkill(o1)) { return -1; }
      if (WeaponSkillCancel.isThisSkill(o2)) { return 1; }

      if (o1.getSkillLevel() != o2.getSkillLevel()) { return o1.getSkillLevel() - o2.getSkillLevel(); }
      return o1.getId().compareTo(o2.getId());
    }
  });

  /**
   * SkillListを取得
   *
   * @return
   */
  public static Collection<WeaponSkillInterface> getSortedSkillList() {
    return skillLevelSkillMap;
  }

  /**
   * 武器スキルを登録する
   *
   * @param weaponSkill
   */
  public static void regist(WeaponSkillData data) {
    WeaponSkillInterface weaponSkill = tempInstanceMap.get(data.getId());
    if (weaponSkill == null) {
      DungeonLogger.development("武器スキル：" + data.getName() + "がプログラム内に存在しません");
      return;
    }

    if (weaponSkill instanceof WeaponSkillForOneType) {
      ((WeaponSkillForOneType) weaponSkill).setData(data);
    }
    regist(weaponSkill);
  }

  public static void regist(WeaponSkillInterface weaponSkill) {
    skillMap.put(weaponSkill.getId(), weaponSkill);
    skillLevelSkillMap.add(weaponSkill);

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
    return skillMap.get(id);
  }

  // 一時的なインスンタンスを保持するためのクラス
  static HashMap<String, WeaponSkillInterface> tempInstanceMap = new HashMap<>();

  public static void registTempData(WeaponSkillInterface skill) {
    tempInstanceMap.put(skill.getId(), skill);
  }

  public static void allRegist() {
    regist(new WeaponSkillBlastOff());
    regist(new WeaponSkillBlastOffLevel2());
    regist(new WeaponSkillCancel());
    registTempData(new ArrowStorm());
    registTempData(new LightningOrder());
    registTempData(new BloodyHeal());
    registTempData(new MeteoStrike());
    registTempData(new IceArrow());
    registTempData(new HealRain());
    registTempData(new ProtectionArmor());
    registTempData(new Explosion());
    registTempData(new Lump());
    registTempData(new BlindEye());
    registTempData(new BurstFlame());
    registTempData(new GrandSpike());
    registTempData(new Finale());
    registTempData(new LeafFlare());
    registTempData(new ReleaseAura());
    registTempData(new GravityField());
  }
}
