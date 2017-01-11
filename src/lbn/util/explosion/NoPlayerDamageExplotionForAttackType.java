package lbn.util.explosion;

import lbn.mob.LastDamageManager;
import lbn.mob.SummonPlayerManager;
import lbn.player.AttackType;
import net.minecraft.server.v1_8_R1.DamageSource;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
		if (sourceEntity == null) {
			super.damageEntity(target, d10);
			return;
		}

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
		//プレイヤーによるダメージにする
		((CraftEntity)target).getHandle().damageEntity(DamageSource.playerAttack(((CraftPlayer)sourceEntity).getHandle()), d10);
	}

}
