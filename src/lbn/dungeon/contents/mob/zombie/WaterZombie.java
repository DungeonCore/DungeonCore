package lbn.dungeon.contents.mob.zombie;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;

public class WaterZombie extends AbstractZombie{

	@Override
	public boolean isIgnoreWater() {
		return true;
	}

	@Override
	public String getName() {
		return "WATER ZOMBIE";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		e.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
		DamageCause cause = e.getCause();
		if (cause == DamageCause.DROWNING) {
			e.setCancelled(true);
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
