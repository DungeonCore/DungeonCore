package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;

import lbn.mob.mobskill.MobSkillRunnable;

public class MobSkillNothing extends MobSkillRunnable{

	public MobSkillNothing(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
	}

}
