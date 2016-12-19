package main.dungeon.contents.mob.zombie;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractZombie;
import main.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NormalZombie extends AbstractZombie{

	@Override
	public String getName() {
		return "Zombie";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntityUtil.removeEquipment(e.getEntity());
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
