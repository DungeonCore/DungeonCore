package main.dungeon.contents.mob.zombie;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractZombie;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Hermit extends AbstractZombie {

	@Override
	public String getName() {
		return "Hermit";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {

	}

	ParticleData particleData = new ParticleData(ParticleType.lava, 10);
	ParticleData particleDataDeffence = new ParticleData(ParticleType.witchMagic, 10);
	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		double maxHealth = ((Damageable)mob).getMaxHealth();
		double nowHealth = ((Damageable)mob).getHealth();
		if (maxHealth / 2.0 > nowHealth) {
			e.setDamage(e.getDamage() * 1.5);
			particleData.run(mob.getLocation().add(0, 1, 0));
			mob.getWorld().playSound(mob.getLocation(), Sound.HORSE_SKELETON_HIT, 1, (float) 0.5);
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		double maxHealth = ((Damageable)mob).getMaxHealth();
		double nowHealth = ((Damageable)mob).getHealth();
		if (maxHealth / 2.0 > nowHealth) {
			e.setDamage(e.getDamage() * 0.8);
			particleDataDeffence.run(mob.getLocation().add(0, 1, 0));
			mob.getWorld().playSound(mob.getLocation(), Sound.FIRE_IGNITE, 1, (float) 0.5);
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {

	}
}
