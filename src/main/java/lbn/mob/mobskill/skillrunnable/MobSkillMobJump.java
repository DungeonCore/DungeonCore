package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import lbn.dungeoncore.Main;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;

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
