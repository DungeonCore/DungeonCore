package lbn.dungeon.contents.mob.zombie;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

public class Ninja extends AbstractZombie{

	@Override
	public String getName() {
		return "Ninja";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		entity.setHealth(1.0);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));
		LivingEntityUtil.setEquipment(entity, new ItemStack(Material.DAYLIGHT_DETECTOR), null, null, null, 0);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		e.setDamage(20.0);
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
