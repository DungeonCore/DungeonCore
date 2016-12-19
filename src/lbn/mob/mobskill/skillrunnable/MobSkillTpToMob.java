package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;

import lbn.mob.mobskill.MobSkillRunnable;

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
