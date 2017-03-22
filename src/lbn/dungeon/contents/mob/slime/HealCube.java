package lbn.dungeon.contents.mob.slime;

import java.util.List;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;
import lbn.mob.AbstractMob;
import lbn.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class HealCube extends AbstractMob<Slime>{

	@Override
	public String getName() {
		return "Heal Cube";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}
	static ParticleData particleData = new ParticleData(ParticleType.heart, 20);

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		if (rnd.nextInt(3) != 0) {
			return;
		}
		//3回に一度回復させる
		List<Entity> nearbyEntities = mob.getNearbyEntities(5, 3, 5);
		for (Entity entity : nearbyEntities) {
			if (LivingEntityUtil.isEnemy(entity)) {
				LivingEntityUtil.addHealth((LivingEntity) entity, 5.0);
				particleData.run(((LivingEntity) entity).getEyeLocation());
			}
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	protected Slime spawnPrivate(Location loc) {
		Slime spawnPrivate = super.spawnPrivate(loc);
		spawnPrivate.setSize(2);
		return spawnPrivate;
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {

	}

	@Override
	public EntityType getEntityType() {
		return EntityType.SLIME;
	}

}
