package lbn.common.explosion;

import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.mob.SummonPlayerManager;
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
	LastDamageMethodType type;

	public NoPlayerDamageExplotionForAttackType(Location l, float f, LivingEntity p, LastDamageMethodType type) {
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

		//爆発を起こしたのがPlayerのとき
		if (sourceEntity.getType() == EntityType.PLAYER) {
			LastDamageManager.onDamage((LivingEntity) target, (Player)sourceEntity, type);
		//SummonMobのとき
		}else if (SummonPlayerManager.isSummonMob(sourceEntity)) {
			Player owner = SummonPlayerManager.getOwner(sourceEntity);
			if (owner != null) {
				LastDamageManager.onDamage((LivingEntity) target, owner, LastDamageMethodType.fromAttackType(SummonPlayerManager.getItemType(owner), true));
			}
		}
		//プレイヤーによるダメージにする
		((CraftEntity)target).getHandle().damageEntity(DamageSource.playerAttack(((CraftPlayer)sourceEntity).getHandle()), d10);
	}

}
