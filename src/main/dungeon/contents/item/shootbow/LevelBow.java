package main.dungeon.contents.item.shootbow;

import main.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.itemAbstract.BowItem;

public abstract class LevelBow extends BowItem {
	abstract protected String[] getStrengthDetail2(int level);

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new NormalWeaponStrengthTemplate(getAvailableLevel(), getMaxStrengthCount());
	}

}
