package lbn.util.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import lbn.mob.SummonPlayerManager;

public class NotMonsterDamageExplosion extends AbstractNotDamageExplosion {

	public NotMonsterDamageExplosion(Location l, float f) {
		super(l, f);
	}

	@Override
	boolean isNotDamage(Entity entity) {
		return entity.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(entity);
	}

}
