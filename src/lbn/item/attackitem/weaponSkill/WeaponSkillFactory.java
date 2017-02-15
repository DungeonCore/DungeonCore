package lbn.item.attackitem.weaponSkill;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import lbn.item.attackitem.weaponSkill.imple.WeaponSkillBlastOff;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillBlastOffLevel2;

public class WeaponSkillFactory {
	private static HashMap<String, WeaponSkillInterface> skillMap = new HashMap<String, WeaponSkillInterface>();

	private static TreeMap<Integer, WeaponSkillInterface> skillLevelSkillMap = new TreeMap<>();

	/**
	 * SkillListを取得
	 * @return
	 */
	public static Collection<WeaponSkillInterface> getSortedSkillList() {
		return skillLevelSkillMap.values();
	}

	/**
	 *武器スキルを登録する
	 * @param weaponSkill
	 */
	public static void regist(WeaponSkillInterface weaponSkill) {
		skillMap.put(weaponSkill.getId(), weaponSkill);
		skillLevelSkillMap.put(weaponSkill.getSkillLevel(), weaponSkill);
	}

	/**
	 *武器スキルを取得する
	 * @param id 武器スキルID
	 */
	public static WeaponSkillInterface getWeaponSkill(String id) {
		return skillMap.get(id);
	}

	static {
		regist(new WeaponSkillBlastOff());
		regist(new WeaponSkillBlastOffLevel2());
	}
}
