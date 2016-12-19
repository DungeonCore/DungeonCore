package lbn.dungeon.contents.item.shootbow;

import lbn.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.itemAbstract.BowItem;

public abstract class LevelBow extends BowItem {
	abstract protected String[] getStrengthDetail2(int level);

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new NormalWeaponStrengthTemplate(getAvailableLevel(), getMaxStrengthCount());
	}

}
