package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

public class MobSkillNothing extends MobSkillRunnable {

  public MobSkillNothing(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {}

}
