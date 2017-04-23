package lbn.mob.mobskill.skillrunnable;

import lbn.common.explosion.NotMonsterDamageExplosion;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;

import org.bukkit.entity.Entity;

public class MobSkillExplosionNoParticle extends MobSkillRunnable{

	int explosionSize;
	public MobSkillExplosionNoParticle(String data) {
		super(data);
		explosionSize = JavaUtil.getInt(data, 2);
	}

	@Override
	public void execute(Entity target, Entity mob) {
		NotMonsterDamageExplosion notMonsterDamageExplosion = new NotMonsterDamageExplosion(target.getLocation(), explosionSize);
		//パーティクルなくす
		notMonsterDamageExplosion.setRunParticle(false);
		//爆発を起こす
		notMonsterDamageExplosion.runExplosion();
	}

}
