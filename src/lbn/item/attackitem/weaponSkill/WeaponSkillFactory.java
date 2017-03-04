package lbn.item.attackitem.weaponSkill;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.item.attackitem.weaponSkill.imple.all.WeaponSkillBlastOff;
import lbn.item.attackitem.weaponSkill.imple.all.WeaponSkillBlastOffLevel2;
import lbn.item.attackitem.weaponSkill.imple.bow.ArrowStorm;
import lbn.item.attackitem.weaponSkill.imple.bow.IceArrow;
import lbn.item.attackitem.weaponSkill.imple.magic.HealRain;
import lbn.item.attackitem.weaponSkill.imple.magic.MeteoStrike;
import lbn.item.attackitem.weaponSkill.imple.sword.BloodyHeal;
import lbn.item.attackitem.weaponSkill.imple.sword.LightningOrder;

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

	static {
		regist(new WeaponSkillBlastOff());
		regist(new WeaponSkillBlastOffLevel2());
		regist(new ArrowStorm());
		regist(new LightningOrder());
		regist(new BloodyHeal());
		regist(new MeteoStrike());
		regist(new IceArrow());
		regist(new HealRain());
	}
}
