package lbn.mob.mobskill.skillrunnable;

import lbn.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.Entity;

public class MobSkillNothing extends MobSkillRunnable{

	public MobSkillNothing(String data) {
		super(data);
	}

	@Override
	public void execute(Entity target, Entity mob) {
	}

}
