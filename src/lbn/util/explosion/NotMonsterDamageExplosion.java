package lbn.util.explosion;

import lbn.mob.SummonPlayerManager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class NotMonsterDamageExplosion extends AbstractNotDamageExplosion {

	public NotMonsterDamageExplosion(Location l, float f) {
		super(l, f);
	}


	boolean isRunParticle = true;
	/**
	 * 爆発のパーティクルを発生させるかどうかセットする
	 * @param isRunParticle
	 */
	public void setRunParticle(boolean isRunParticle) {
		this.isRunParticle = isRunParticle;
	}

	@Override
	protected boolean isRunParticle() {
		return isRunParticle;
	}

	@Override
	boolean isNotDamage(Entity entity) {
		return entity.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(entity);
	}

}
