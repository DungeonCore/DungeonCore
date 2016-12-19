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
import lbn.mob.LastDamageManager;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.player.AttackType;
import lbn.util.LivingEntityUtil;

public class Bondage extends AbstractZombie{

	@Override
	public String getName() {
		return "Bondage";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		LivingEntityUtil.setEquipment(entity, new ItemStack(Material.MOB_SPAWNER), new ItemStack(Material.CHAINMAIL_CHESTPLATE),
				new ItemStack(Material.CHAINMAIL_LEGGINGS) ,new ItemStack(Material.CHAINMAIL_BOOTS), 0);
		LivingEntityUtil.setItemInHand(entity, new ItemStack(Material.IRON_AXE), 0);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000000, 1));
	}

	@Override
	public double getNoKnockback() {
		return 1.0;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		e.setDamage(30.0);
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		AttackType lastDamageAttackType = LastDamageManager.getLastDamageAttackType(mob);
		if (lastDamageAttackType != AttackType.SWORD) {
			e.setCancelled(true);
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {

	}

}
