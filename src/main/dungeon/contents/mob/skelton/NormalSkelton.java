package main.dungeon.contents.mob.skelton;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractSkelton;
import main.util.LivingEntityUtil;

public class NormalSkelton extends AbstractSkelton{

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public String getName() {
		return "Skeleton";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntityUtil.removeEquipment(e.getEntity(), false);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
