package main.dungeon.contents.mob.skelton;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractSkelton;
import main.util.ItemStackUtil;
import main.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Arion extends AbstractSkelton{
	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public String getName() {
		return "Arion";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		Skeleton entity = (Skeleton) e.getEntity();
		entity.setSkeletonType(SkeletonType.WITHER);

		LivingEntityUtil.setEquipment(entity,
				new ItemStack[]{new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS)},
				0);
		ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
		itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		itemStack.addEnchantment(Enchantment.FIRE_ASPECT, 1);
		LivingEntityUtil.setItemInHand(entity, itemStack, 0);

		entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 1));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		if (damager instanceof LivingEntity) {
			ItemStack itemInHand = ((LivingEntity)damager).getEquipment().getItemInHand();
			if (ItemStackUtil.isSword(itemInHand)) {
				e.setCancelled(true);
				mob.getWorld().playSound(mob.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.3F);
			}
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
