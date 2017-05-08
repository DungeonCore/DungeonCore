package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class MobSkillHealMob extends MobSkillRunnable {

  public MobSkillHealMob(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    if (mob.getType().isAlive()) {
      Stun.addStun((LivingEntity) mob, 20 * 2);

      double parsent = 20;
      try {
        parsent = Double.parseDouble(data);
      } catch (Exception e) {}

      LivingEntityUtil.addHealth((LivingEntity) mob, ((Damageable) mob).getMaxHealth() * parsent * 0.01);
    }

  }

}
