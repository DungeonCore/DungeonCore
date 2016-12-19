package main.dungeon.contents.mob.zombie;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractZombie;
import main.util.LivingEntityUtil;
import main.util.explosion.MobAttackExplosion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SuicideAttackZombie extends AbstractZombie{
	@Override
	public String getName() {
		return "Suicide Zombie";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		LivingEntityUtil.removeEquipment(entity);
		entity.setMaxHealth(0.5);
		entity.setHealth(0.5);
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
		new MobAttackExplosion(e.getEntity().getLocation(), 5, e.getEntity()).runExplosion();
	}

}
