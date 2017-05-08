package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.common.explosion.NotMonsterDamageExplosion;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.entity.Entity;

public class MobSkillExplosion extends MobSkillRunnable {

  int explosionSize;

  public MobSkillExplosion(String data) {
    super(data);
    explosionSize = JavaUtil.getInt(data, 2);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    new NotMonsterDamageExplosion(target.getLocation(), explosionSize).runExplosion();
  }

}
