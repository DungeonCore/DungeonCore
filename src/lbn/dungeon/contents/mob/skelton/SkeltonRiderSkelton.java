package lbn.dungeon.contents.mob.skelton;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractSkelton;

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
