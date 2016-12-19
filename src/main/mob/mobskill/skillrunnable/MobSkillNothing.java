package main.mob.mobskill.skillrunnable;

import main.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.LivingEntity;

public class MobSkillNothing extends MobSkillRunnable{

	public MobSkillNothing(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
	}

}
