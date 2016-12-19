package main.dungeon.contents.mob.zombie;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.lbn.Main;
import main.mob.mob.abstractmob.AbstractZombie;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MineralizationSoldier extends AbstractZombie{
	@Override
	public String getName() {
		return "Mineralization Soldier";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 1));
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public void onTarget(EntityTargetLivingEntityEvent event) {
		Entity zombie = event.getEntity();

		if (zombie.hasMetadata("LBN_MineralizationSoldier_TARGET")) {
			return;
		}

		Entity entity = event.getTarget();
		if (entity instanceof LivingEntity) {
			zombie.setMetadata("LBN_MineralizationSoldier_TARGET", new FixedMetadataValue(Main.plugin, "true"));
			LivingEntity e = (LivingEntity) entity;
			e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 114), true);
			e.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 130), true);
			e.getWorld().playSound(e.getLocation(), Sound.SLIME_WALK2, 1f, 10f);
		}
	}

}
