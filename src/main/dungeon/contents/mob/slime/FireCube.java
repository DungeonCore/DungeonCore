package main.dungeon.contents.mob.slime;

import java.util.List;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.AbstractMob;
import main.util.LivingEntityUtil;
import main.util.particle.CircleParticleData;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

public class FireCube extends AbstractMob<Slime>{

	@Override
	public String getName() {
		return "Fire Cube";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}

	static CircleParticleData particleData = new CircleParticleData(new ParticleData(ParticleType.flame, 10).setDispersion(0, 0, 0), 8);

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		if (rnd.nextInt(3) != 0) {
			return;
		}

		//3回に一度回復させる
		List<Entity> nearbyEntities = mob.getNearbyEntities(8, 3, 8);
		for (Entity entity : nearbyEntities) {
			if (entity.getType().isAlive() && !LivingEntityUtil.isEnemy(entity)) {
				((LivingEntity) entity).setFireTicks(20 * 3);
			}
		}
		particleData.run(mob.getLocation().add(0, 0.3, 0));
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
		DamageCause cause = e.getCause();
		if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {
			e.setCancelled(true);
		}
	}

	@Override
	protected Slime spawnPrivate(Location loc) {
		Slime spawnPrivate = super.spawnPrivate(loc);
		spawnPrivate.setSize(2);
		return spawnPrivate;
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {

	}

	@Override
	public EntityType getEntityType() {
		return EntityType.SLIME;
	}

}
