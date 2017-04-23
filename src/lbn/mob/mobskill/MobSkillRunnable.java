package lbn.mob.mobskill;

import org.bukkit.entity.Entity;

public abstract class MobSkillRunnable {
	protected String data;

	public MobSkillRunnable(String data) {
		this.data = data;
	}
	abstract public void execute(Entity target, Entity mob);
}
