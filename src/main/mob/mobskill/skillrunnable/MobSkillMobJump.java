package main.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import main.mob.mobskill.MobSkillRunnable;
import main.util.JavaUtil;

public class MobSkillMobJump extends MobSkillRunnable{

	public MobSkillMobJump(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		double double1 = JavaUtil.getDouble(data, 2);
		mob.setVelocity(new Vector(0, double1, 0));
	}

}
