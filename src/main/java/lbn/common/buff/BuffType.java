package lbn.common.buff;

import org.bukkit.potion.PotionEffectType;

public enum BuffType {
	SLOW(1, PotionEffectType.SLOW, "移動速度減少"),
	CONFUSION(1, PotionEffectType.CONFUSION, "吐き気"),
	BLINDNESS(1, PotionEffectType.BLINDNESS, "盲目"),
	HUNGER(1, PotionEffectType.HUNGER, "空腹"),
	WEAKNESS(1, PotionEffectType.WEAKNESS, "弱体化"),
	POISON(1, PotionEffectType.POISON, "毒"),
	WITHER(1, PotionEffectType.WITHER, "ウィザー"),
	HEALTH_DECREATE(-1, PotionEffectType.HEALTH_BOOST, "体力減少"),
	HEALTH_BOOST(1, PotionEffectType.HEALTH_BOOST, "体力増加"),
	SPEED(1, PotionEffectType.SPEED, "移動速度上昇"),
	FAST_DIGGING(1, PotionEffectType.FAST_DIGGING, "採掘速度上昇"),
	SLOW_DIGGING(1, PotionEffectType.SLOW_DIGGING, "採掘速度低下"),
	INCREASE_DAMAGE(1, PotionEffectType.INCREASE_DAMAGE, "ダメージ上昇"),
	HEAL(1, PotionEffectType.HEAL, "即時回復"),
	HARM(1, PotionEffectType.HARM, "ダメージ"),
	JUMP(1, PotionEffectType.JUMP, "跳躍力上昇"),
	REGENERATION(1, PotionEffectType.REGENERATION, "再生能力"),
	DAMAGE_RESISTANCE(1, PotionEffectType.DAMAGE_RESISTANCE, "耐性"),
	FIRE_RESISTANCE(1, PotionEffectType.FIRE_RESISTANCE, "火炎耐性"),
	WATER_BREATHING(1, PotionEffectType.WATER_BREATHING, "水中呼吸"),
	INVISIBILITY(1, PotionEffectType.INVISIBILITY, "透明化"),
	NIGHT_VISION(1, PotionEffectType.NIGHT_VISION, "暗視"),
	ABSORPTION(1, PotionEffectType.ABSORPTION, "衝撃吸収"),
	SATURATION(1, PotionEffectType.SATURATION, "満腹度回復");

	private BuffType(int num, PotionEffectType type, String jpname) {
		this.num = num;
		this.type = type;
		this.jpname = jpname;
	}

	int num;
	PotionEffectType type;
	String jpname;

	public static BuffType getDebuffType(String jpname) {
		if (jpname == null || jpname.isEmpty()) {
			return null;
		}
		for (BuffType debuff : values()) {
			if (debuff.jpname.equals(jpname)) {
				return debuff;
			}
		}
		return null;
	}

	public int getNum() {
		return num;
	}

	public PotionEffectType getType() {
		return type;
	}

}
