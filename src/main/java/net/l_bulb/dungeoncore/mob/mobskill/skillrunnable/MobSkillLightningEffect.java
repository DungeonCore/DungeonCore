package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class MobSkillLightningEffect extends MobSkillRunnable {

  public MobSkillLightningEffect(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    strikeLightningEffect(target, mob);
  }

  public static void strikeLightningEffect(Entity target, Entity mob) {
    LivingEntityUtil.strikeLightningEffect(target.getLocation());
  }

}
