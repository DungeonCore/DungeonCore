package lbn.dungeon.contents.mob.zombie;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class NecroWarrior extends AbstractZombie {
	@Override
	public String getName() {
		return "Necro Warrior";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		EntityEquipment equipment = e.getEntity().getEquipment();
		LivingEntityUtil.removeEquipment(e.getEntity());
		ItemStack helmet = new ItemStack(Material.FURNACE);
		equipment.setHelmet(helmet);
		equipment.setHelmetDropChance(0);
		e.getEntity().setMaxHealth(1000.0);
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
