package main.dungeon.contents.mob.skelton;

import java.util.List;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractSkelton;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SkeltonRiderSkelton extends AbstractSkelton{

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	public String getName() {
		return "Rider";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
		List<Entity> nearbyEntities = e.getEntity().getNearbyEntities(2, 2, 2);
		for (Entity entity : nearbyEntities) {
			if (entity.getType() == EntityType.BAT && entity.getPassenger() == null) {
				entity.remove();
			}
		}
	}
}
