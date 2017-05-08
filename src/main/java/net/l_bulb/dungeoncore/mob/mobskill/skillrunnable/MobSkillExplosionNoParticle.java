package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.common.explosion.NotMonsterDamageExplosion;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.entity.Entity;

public class MobSkillExplosionNoParticle extends MobSkillRunnable {

  int explosionSize;

  public MobSkillExplosionNoParticle(String data) {
    super(data);
    explosionSize = JavaUtil.getInt(data, 2);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    NotMonsterDamageExplosion notMonsterDamageExplosion = new NotMonsterDamageExplosion(target.getLocation(), explosionSize);
    // パーティクルなくす
    notMonsterDamageExplosion.setRunParticle(false);
    // 爆発を起こす
    notMonsterDamageExplosion.runExplosion();
  }

}
