package main.mob.mobskill;

import org.bukkit.potion.PotionEffectType;

public enum DebuffType {
	SLOW(1, PotionEffectType.SLOW, "移動速度減少"),
	CONFUSION(1, PotionEffectType.CONFUSION, "吐き気"),
	BLINDNESS(1, PotionEffectType.BLINDNESS, "盲目"),
	HUNGER(1, PotionEffectType.HUNGER, "空腹"),
	WEAKNESS(1, PotionEffectType.WEAKNESS, "弱体化"),
	POISON(1, PotionEffectType.POISON, "毒"),
	WITHER(1, PotionEffectType.WITHER, "ウィザー"),
	HEALTH_BOOST(-1, PotionEffectType.HEALTH_BOOST, "体力減少");

	private DebuffType(int num, PotionEffectType type, String jpname) {
		this.num = num;
		this.type = type;
		this.jpname = jpname;
	}

	int num;
	PotionEffectType type;
	String jpname;

	public static DebuffType getDebuffType(String jpname) {
		if (jpname == null || jpname.isEmpty()) {
			return null;
		}
		for (DebuffType debuff : values()) {
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
