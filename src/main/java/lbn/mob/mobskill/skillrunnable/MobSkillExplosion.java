package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.Entity;

import lbn.common.explosion.NotMonsterDamageExplosion;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;

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
