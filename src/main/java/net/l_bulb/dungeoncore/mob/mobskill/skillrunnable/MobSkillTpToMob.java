package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

public class MobSkillTpToMob extends MobSkillRunnable {

  public MobSkillTpToMob(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    if (mob.isValid() && target.isValid()) {
      Vector vectorMob = mob.getLocation().toVector();
      Vector vectorDamager = target.getLocation().toVector();
      target.setVelocity(vectorDamager.subtract(vectorMob).multiply(-0.5));
    }
  }

}
