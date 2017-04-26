package lbn.common.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class MobAttackExplosion extends NotMonsterDamageExplosion{

	public MobAttackExplosion(Location l, float f, LivingEntity mob) {
		super(l, f);
		this.mob = mob;
	}

	LivingEntity mob;

	@Override
	public void damageEntity(Entity craftEntity, float d10) {
		if (craftEntity.getType().isAlive()) {
			((LivingEntity)craftEntity).damage(d10, craftEntity);
		}
	}
}
