package lbn.mob.mobskill.skillrunnable;

import lbn.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MobSkillKeepAwayTarget extends MobSkillRunnable {

	public MobSkillKeepAwayTarget(String data) {
		super(data);
	}

	@Override
	public void execute(Entity target, Entity mob) {
		Vector vectorMob = mob.getLocation().toVector();
		Vector vectorDamager = target.getLocation().toVector();
		target.setVelocity(vectorDamager.subtract(vectorMob).normalize().multiply(3).setY(0));
	}
}
