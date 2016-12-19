package main.mob.mobskill;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;

public enum MobSkillExcuteTimingType {
	ALWAYS("体力関係なし"),
	HP_OVER_50PER("体力が50％以上"),
	HP_OVER_75PER("体力が75％以上"),
	HP_UNDER_50PER("体力が50％以下"),
	HP_UNDER_25PER("体力が25％以下");

	String detail;
	private MobSkillExcuteTimingType(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public static MobSkillExcuteTimingType getInstance(String detail) {
		for (MobSkillExcuteTimingType val : values()) {
			if (val.getDetail().equals(detail)) {
				return val;
			}
		}
		return null;
	}

	public boolean canExecute(LivingEntity e) {
		double health = ((Damageable)e).getHealth();
		double maxHealth = ((Damageable)e).getMaxHealth();
		switch (this) {
		case HP_OVER_50PER:
			return health > maxHealth * 0.5;
		case HP_OVER_75PER:
			return health > maxHealth * 0.75;
		case HP_UNDER_50PER:
			return health < maxHealth * 0.50;
		case HP_UNDER_25PER:
			return health < maxHealth * 0.25;
		default:
			return true;
		}
	}
}
