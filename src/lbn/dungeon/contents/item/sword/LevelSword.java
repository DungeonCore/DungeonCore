package lbn.dungeon.contents.item.sword;

import lbn.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.itemAbstract.SwordItem;
import lbn.item.strength.StrengthOperator;

import org.bukkit.entity.LivingEntity;

public abstract class LevelSword extends SwordItem{

	protected int getStrengthLevel(LivingEntity e) {
		return StrengthOperator.getLevel(e.getEquipment().getItemInHand());
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new NormalWeaponStrengthTemplate(getAvailableLevel(), getMaxStrengthCount());
	}

	@Override
	protected int getBaseBuyPrice() {
		return 100;
	}
}
