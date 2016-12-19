package main.dungeon.contents.mob.skelton;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.SaveMobEntity;
import main.mob.mob.abstractmob.AbstractSkelton;
import main.util.LivingEntityUtil;

public class KumajyagaSkelton extends AbstractSkelton implements SaveMobEntity{

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public String getName() {
		return "墓に眠りし弓兵";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		LivingEntityUtil.setEquipment(entity,
				new ItemStack(Material.IRON_HELMET),
				new ItemStack(Material.IRON_CHESTPLATE),
				new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_BOOTS), 0);

		ItemStack itemStack = new ItemStack(Material.BOW);
		itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
		itemStack.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		LivingEntityUtil.setItemInHand(entity, itemStack, 0);

		if (KumajyagaSkelton.entity != null) {
			KumajyagaSkelton.entity.remove();
		}
		KumajyagaSkelton.entity = entity;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		entity = null;
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {

	}

	static LivingEntity entity = null;

	@Override
	public List<LivingEntity> getAliveEntity() {
		return Arrays.asList(entity);
	}

	@Override
	public void clearAllMob() {
		if (entity != null) {
			entity.remove();
		}
	}

	@Override
	public void onDisablePlugin(PluginDisableEvent e) {
		if (entity != null) {
			entity.remove();
		}
	}
}
