package main.mob.mobskill.skillrunnable;

import main.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.LivingEntity;

public class MobSkillTpToMob extends MobSkillRunnable{

	public MobSkillTpToMob(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		if (mob.isValid() && target.isValid()) {
			target.teleport(mob);
		}
	}

}
