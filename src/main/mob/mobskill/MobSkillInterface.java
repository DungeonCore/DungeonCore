package main.mob.mobskill;

import org.bukkit.entity.LivingEntity;

public interface MobSkillInterface {
	public MobSkillExcuteTimingType getTiming();

	public MobSkillExcuteConditionType getCondtion();

	public MobSkillTargetingMethodType getTargetingMethod();

	public String getName();

	public int excutePercent();

	public void execute(LivingEntity target, LivingEntity mob);
}
