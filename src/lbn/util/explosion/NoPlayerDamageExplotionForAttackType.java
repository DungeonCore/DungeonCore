package lbn.util.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lbn.mob.LastDamageManager;
import lbn.mob.SummonPlayerManager;
import lbn.player.AttackType;

public class NoPlayerDamageExplotionForAttackType extends NotPlayerDamageExplosion{

	LivingEntity sourceEntity;
	AttackType type;

	public NoPlayerDamageExplotionForAttackType(Location l, float f, LivingEntity p, AttackType type) {
		super(l, f);
		this.sourceEntity = p;
		this.type = type;
	}

	@Override
	public void damageEntity(Entity target, float d10) {
		if (target instanceof LivingEntity) {
			if (sourceEntity.getType() == EntityType.PLAYER) {
				LastDamageManager.onDamage((LivingEntity) target, (Player)sourceEntity, type);
			}else if (sourceEntity instanceof LivingEntity) {
				Player owner = SummonPlayerManager.getOwner(sourceEntity);
				if (owner != null) {
					LastDamageManager.onDamage((LivingEntity) target, owner, AttackType.MAGIC);
				}
			}
		}
		super.damageEntity(target, d10);
	}

}
