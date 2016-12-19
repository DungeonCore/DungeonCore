package main.mob.mobskill.skillrunnable;

import main.lbn.Main;
import main.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MobSkillUpperTarget extends MobSkillRunnable{

	public MobSkillUpperTarget(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		new BukkitRunnable() {
			@Override
			public void run() {
				target.setVelocity(new Vector(0, 0.6, 0));
			}
		}.runTaskLater(Main.plugin, 2);
	}

}
