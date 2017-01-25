package lbn.player;

import lbn.util.ItemStackUtil;

import org.bukkit.Material;

public enum AttackType {
	SWORD(TheLowLevelType.SWORD, 7, ItemStackUtil.getVanillaDamage(Material.WOOD_SWORD)),
	BOW(TheLowLevelType.BOW, 14, ItemStackUtil.getVanillaDamage(Material.BOW)),
	MAGIC(TheLowLevelType.BOW, 9, 6),
	OTHER(),
	IGNORE();


	private AttackType(TheLowLevelType type, double expectedValue, double level0MinDamage) {
		this.levelType = type;
		this.expectedValue = expectedValue;
		this.level0MinDamage = level0MinDamage;
		isDamageCaluculate = true;
	}

	private AttackType() {
		this.levelType = TheLowLevelType.MAIN;
	}


	boolean isDamageCaluculate = false;
	/**
	 * レベルごとのダメージの計算を行うならTRUE
	 * @return
	 */
	public boolean isDamageCaluculate() {
		return isDamageCaluculate;
	}

	double level0MinDamage = 4;

	TheLowLevelType levelType;

	 double expectedValue = 7;

	public TheLowLevelType getLevelType() {
		return levelType;
	}

	public double getExpectedValue() {
		return expectedValue;
	}

	public double getDamagePer(int level) {
		if (isDamageCaluculate()) {
			double min = getMinDamage(level);
			double max = getMaxDamage(level);

			double aveDamage = (min + max) / 2;

			double damagePer = getExpectedValue() / aveDamage;

			return damagePer;
		} else {
			return 1;
		}
	}

	public double getMinDamage(int level) {
		return level0MinDamage + level / 20.0;
	}

	public double getMaxDamage(int level) {
		if (this == SWORD) {
			return  8 + Math.pow(level / 10.0, 2);
		} else if (this == BOW) {
			return 13 + Math.pow(level / 10.0, 2) * 1.3;
		} else if (this == MAGIC) {
			return 13 + Math.pow(level / 10.0, 2) * 1.3;
		}

		return 8 + Math.pow(level / 10.0, 2);
	}
}
