package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MobSkillTpToTarget extends MobSkillRunnable {

  public MobSkillTpToTarget(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    if (mob.isValid() && target.isValid()) {
      Location location = target.getLocation();
      location.setDirection(location.getDirection().multiply(-1));

      Vector vectorMob = mob.getLocation().toVector();
      Vector vectorDamager = target.getLocation().toVector();
      target.setVelocity(vectorDamager.subtract(vectorMob).multiply(-0.5));
    }
  }

}
