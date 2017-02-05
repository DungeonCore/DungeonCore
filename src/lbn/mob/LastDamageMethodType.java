package lbn.mob;

import lbn.api.TheLowLevelType;
import lbn.player.ItemType;

/**
 * プレイヤーがモンスターに攻撃をした攻撃方法
 */
public enum LastDamageMethodType {
	SWORD("剣", TheLowLevelType.SWORD),	//剣で攻撃
	BOW("弓", TheLowLevelType.BOW),		//弓で攻撃
	MAGIC("魔法", TheLowLevelType.MAGIC),		//魔法で攻撃
	SWORD_SUMMON("剣(Summon)", TheLowLevelType.SWORD),		//剣で召喚したSummonモンスターが攻撃
	BOW_SUMMON("弓(Summon)", TheLowLevelType.BOW),			//弓で召喚したSummonモンスターが攻撃
	MAGIC_SUMMON("魔法(Summon)", TheLowLevelType.MAGIC),		//魔法で召喚したSummonモンスターが攻撃
	BARE_HAND("素手"),				//素手
	MELEE_ATTACK_WITH_OTHER("アイテムで殴る"),	//その他のアイテムを持って直接攻撃
	USE_ITEM("アイテム使用"),		//アイテムを使って攻撃
	OTHER("その他");				//その他の方法

	String text;
	TheLowLevelType levelType;

	private LastDamageMethodType(String text) {
		this(text, null);
	}

	private LastDamageMethodType(String text, TheLowLevelType type) {
		this.text = text;
		this.levelType = type;
	}

	/**
	 * 日本語による説明を取得
	 * @return
	 */
	public String getText() {
		return text;
	}

	public static LastDamageMethodType fromAttackType(ItemType type) {
		return fromAttackType(type, false);
	}

	public static LastDamageMethodType fromAttackType(ItemType type, boolean isSummon) {
		switch (type) {
		case SWORD:
			if (isSummon) {
				return SWORD_SUMMON;
			} else {
				return SWORD;
			}
		case BOW:
			if (isSummon) {
				return BOW_SUMMON;
			} else {
				return BOW;
			}
		case MAGIC:
			if (isSummon) {
				return MAGIC_SUMMON;
			} else {
				return MAGIC;
			}
		default:
			return OTHER;
		}
	}

	/**
	 * 攻撃方法に対応するLevelTypeを取得。どれにも当てはまらない場合はnullを返す
	 * @return
	 */
	public TheLowLevelType getLevelType() {
		return levelType;
	}
}
