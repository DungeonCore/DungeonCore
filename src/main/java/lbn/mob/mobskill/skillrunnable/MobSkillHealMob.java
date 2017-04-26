package lbn.mob.mobskill.skillrunnable;

import lbn.common.other.Stun;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.LivingEntityUtil;

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
