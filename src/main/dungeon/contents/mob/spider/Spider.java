package main.dungeon.contents.mob.spider;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractSpider;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Spider extends AbstractSpider {

	@Override
	public String getName() {
		return "Spider";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {

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
