package main.mob.mobskill.skillrunnable;

import main.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class MobSkillKeepAwayTarget extends MobSkillRunnable{

	public MobSkillKeepAwayTarget(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		Vector vectorMob = mob.getLocation().toVector();
		Vector vectorDamager = target.getLocation().toVector();
		target.setVelocity(vectorDamager.subtract(vectorMob).normalize().multiply(3).setY(0));
	}
}
