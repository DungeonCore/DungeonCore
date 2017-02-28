package lbn.item.attackitem.weaponSkill.imple;

import lbn.item.attackitem.weaponSkill.WeaponSkillInterface;
import lbn.player.ItemType;

public abstract class WeaponSkillForOneType implements WeaponSkillInterface{
	ItemType type;

	public WeaponSkillForOneType(ItemType type) {
		this.type = type;
	}

	@Override
	public boolean canUse(ItemType type) {
		return this.type == type;
	}

}
