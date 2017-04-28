package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

public class MobSkillUpperTargetHight extends MobSkillRunnable {

  public MobSkillUpperTargetHight(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    new BukkitRunnable() {
      @Override
      public void run() {
        target.setVelocity(new Vector(0, 1.4, 0));
      }
    }.runTaskLater(Main.plugin, 2);
  }
}
