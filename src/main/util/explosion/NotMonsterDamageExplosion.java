package main.util.explosion;

import main.mob.SummonPlayerManager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class NotMonsterDamageExplosion extends AbstractNotDamageExplosion {

	public NotMonsterDamageExplosion(Location l, float f) {
		super(l, f);
	}

	@Override
	boolean isNotDamage(Entity entity) {
		return entity.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(entity);
	}

}
