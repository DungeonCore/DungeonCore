package lbn.mob.mobskill;

import org.bukkit.entity.LivingEntity;

public abstract class MobSkillRunnable {
	protected String data;

	public MobSkillRunnable(String data) {
		this.data = data;
	}
	abstract public void execute(LivingEntity target, LivingEntity mob);
}
