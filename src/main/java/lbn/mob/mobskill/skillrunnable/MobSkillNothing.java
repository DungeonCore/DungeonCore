package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;

import lbn.mob.mobskill.MobSkillRunnable;

public class MobSkillNothing extends MobSkillRunnable {

  public MobSkillNothing(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {}

}
