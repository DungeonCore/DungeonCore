package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class MobSkillLightningEffect3 extends MobSkillRunnable {

  public MobSkillLightningEffect3(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    int num = JavaUtil.getInt(data, 3);

    new BukkitRunnable() {
      int count = 0;

      @Override
      public void run() {
        MobSkillLightningEffect.strikeLightningEffect(target, mob);
        count++;
        if (count >= num) {
          cancel();
        }
      }
    }.runTaskTimer(Main.plugin, 0, 10);
  }

}
