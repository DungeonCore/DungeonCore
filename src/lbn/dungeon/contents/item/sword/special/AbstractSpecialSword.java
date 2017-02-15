package lbn.dungeon.contents.item.sword.special;

import lbn.dungeon.contents.item.sword.LevelSword;
import lbn.item.itemInterface.SpecialAttackItemable;

import org.bukkit.Material;

public abstract class AbstractSpecialSword extends LevelSword implements SpecialAttackItemable{

	protected AbstractSpecialSword(Material m, int availableLevel) {
		this.m = m;
		this.availableLevel = availableLevel;
	}

	public AbstractSpecialSword() {
	}

	abstract protected double getSpecialDamagePercent();

	public double getAttackItemDamage(int strengthLevel) {
		return super.getAttackItemDamage(strengthLevel) * getSpecialDamagePercent();
	}

	Material m;
	int availableLevel;

	@Override
	public String getItemName() {
		return getSpecialName() + " level" + getAvailableLevel();
	}

	abstract protected String getBaseId();

	@Override
	public String getId() {
		return getBaseId() + getAvailableLevel();
	}

	@Override
	public int getAvailableLevel() {
		return availableLevel;
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	protected Material getMaterial() {
		return m;
	}

	@Override
	abstract public int getMaxSlotCount();

	@Override
	abstract public int getDefaultSlotCount();

	@Override
	public boolean isShowItemList() {
		return false;
	}
}
