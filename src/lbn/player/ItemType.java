package lbn.player;

import lbn.api.LevelType;
import lbn.util.ItemStackUtil;

import org.bukkit.Material;

public enum ItemType {
	SWORD(LevelType.SWORD, ItemStackUtil.getVanillaDamage(Material.WOOD_SWORD)),
	BOW(LevelType.BOW, ItemStackUtil.getVanillaDamage(Material.BOW)),
	MAGIC(LevelType.BOW, 6),
	OTHER(),
	IGNORE();


	private ItemType(LevelType type, double level0MinDamage) {
		this.levelType = type;
		this.level0MinDamage = level0MinDamage;
	}

	private ItemType() {
		this.levelType = LevelType.MAIN;
	}

	double level0MinDamage = 4;

	LevelType levelType;

	public LevelType getLevelType() {
		return levelType;
	}

	/**
	 * レベルに応じた武器の最小ダメージ
	 * @param level
	 * @return
	 */
	public double getMinDamage(int level) {
		return level0MinDamage + level / 20.0;
	}


	/**
	 * レベルに応じた武器の最大ダメージを取得
	 * @param level
	 * @return
	 */
	public double getMaxDamage(int level) {
		//キャッシュをするほうが遅くなるのでこのまま計算する
		if (level <= 60) {
			if (this == SWORD) {
				return 9 + Math.pow(level / 10.0, 2) * 2.6;
			} else if (this == BOW) {
				return 12 + Math.pow(level / 10.0, 2.1) * 2.7;
			} else if (this == MAGIC) {
				return 12 + Math.pow(level / 10.0, 2) * 2.6;
			}
		} else {
			if (this == SWORD) {
				return 9 + level / 10.0 * 6.6 + 5;
			} else if (this == BOW) {
				return 13 + Math.pow((level - 60) / 10.0, 1.1) * 6.7 + 5;
			} else if (this == MAGIC) {
				return 9 + level / 10.0 * 6.6 + 5;
			}
		}

		//その他の時は起こり得ないが剣にする
		return 12 + Math.pow(level / 10.0, 2) * 2.6;
	}
}
