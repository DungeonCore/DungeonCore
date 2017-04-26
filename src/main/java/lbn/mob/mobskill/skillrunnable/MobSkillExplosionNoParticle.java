package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;

import lbn.common.explosion.NotMonsterDamageExplosion;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;

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
