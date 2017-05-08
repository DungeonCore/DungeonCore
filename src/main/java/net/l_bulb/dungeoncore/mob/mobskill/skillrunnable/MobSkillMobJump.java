package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MobSkillMobJump extends MobSkillRunnable {

  public MobSkillMobJump(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    double double1 = JavaUtil.getDouble(data, 2);
    mob.setVelocity(new Vector(0, double1, 0));

    new BukkitRunnable() {
      @Override
      public void run() {
        if (mob.isOnGround()) {
          cancel();
        }
        mob.setFallDistance(0);
      }
    }.runTaskTimer(Main.plugin, 5, 3);
  }

}
