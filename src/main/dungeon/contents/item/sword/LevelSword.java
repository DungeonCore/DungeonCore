package main.dungeon.contents.item.sword;

import org.bukkit.entity.LivingEntity;

import main.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.itemAbstract.SwordItem;
import main.item.strength.StrengthOperator;

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
