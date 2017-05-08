package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

import org.bukkit.entity.Entity;

public class MobSkillNothing extends MobSkillRunnable {

  public MobSkillNothing(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {}

}
