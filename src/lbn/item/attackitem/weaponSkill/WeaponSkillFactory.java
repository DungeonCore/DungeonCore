package lbn.item.attackitem.weaponSkill;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.item.attackitem.weaponSkill.imple.all.WeaponSkillBlastOff;
import lbn.item.attackitem.weaponSkill.imple.all.WeaponSkillBlastOffLevel2;
import lbn.item.attackitem.weaponSkill.imple.bow.ArrowStorm;
import lbn.item.attackitem.weaponSkill.imple.bow.IceArrow;
import lbn.item.attackitem.weaponSkill.imple.magic.HealRain;
import lbn.item.attackitem.weaponSkill.imple.magic.MeteoStrike;
import lbn.item.attackitem.weaponSkill.imple.sword.BloodyHeal;
import lbn.item.attackitem.weaponSkill.imple.sword.LightningOrder;
import lbn.util.DungeonLogger;

public class WeaponSkillFactory {
	private static HashMap<String, WeaponSkillInterface> skillMap = new HashMap<String, WeaponSkillInterface>();

	private static TreeSet<WeaponSkillInterface> skillLevelSkillMap = new TreeSet<WeaponSkillInterface>(new Comparator<WeaponSkillInterface>() {
		@Override
		public int compare(WeaponSkillInterface o1, WeaponSkillInterface o2) {
			if (o1.getSkillLevel() != o2.getSkillLevel()) {
				return o1.getSkillLevel() - o2.getSkillLevel();
			}
			return o1.getId().compareTo(o2.getId());
		}
	});

	/**
	 * SkillListを取得
	 * @return
	 */
	public static Collection<WeaponSkillInterface> getSortedSkillList() {
		return skillLevelSkillMap;
	}

	/**
	 *武器スキルを登録する
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
			System.out.println("データセット：" + weaponSkill.getName());
		}
		regist(weaponSkill);
	}

	public static void regist(WeaponSkillInterface weaponSkill) {
		System.out.println("登録：" + weaponSkill.getName());
		skillMap.put(weaponSkill.getId(), weaponSkill);
		skillLevelSkillMap.add(weaponSkill);

		if (weaponSkill instanceof ProjectileInterface) {
			ProjectileManager.regist((ProjectileInterface) weaponSkill);
		}
	}

	/**
	 *武器スキルを取得する
	 * @param id 武器スキルID
	 */
	public static WeaponSkillInterface getWeaponSkill(String id) {
		return skillMap.get(id);
	}

	//一時的なインスンタンスを保持するためのクラス
	static HashMap<String, WeaponSkillInterface> tempInstanceMap = new HashMap<String, WeaponSkillInterface>();

	public static void registTempData(WeaponSkillInterface skill) {
		tempInstanceMap.put(skill.getId(), skill);
	}

	public static void allRegist() {
		regist(new WeaponSkillBlastOff());
		regist(new WeaponSkillBlastOffLevel2());
		registTempData(new ArrowStorm());
		registTempData(new LightningOrder());
		registTempData(new BloodyHeal());
		registTempData(new MeteoStrike());
		registTempData(new IceArrow());
		registTempData(new HealRain());
	}
}
