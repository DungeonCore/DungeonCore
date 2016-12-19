package lbn.dungeon.contents.mob.zombie;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeon.contents.item.material.CaveStone;
import lbn.dungeon.contents.item.questItem.Serum;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.player.AttackType;
import lbn.util.LivingEntityUtil;

public class CaveZombie extends AbstractZombie{
	@Override
	public String getName() {
		return "Cave Zombie";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		EntityEquipment equipment = entity.getEquipment();
		equipment.setItemInHand(new ItemStack(Material.IRON_SPADE));
		LivingEntityUtil.removeEquipment(entity);
		entity.setMaxHealth(12.0);
		entity.setHealth(12.0);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * 0.4);
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public double getNearingSpeed() {
		return super.getNearingSpeed() * 0.6;
	}

	@Override
	public ItemStack[] getNormalItem(Player lastDamagePlayer) {
		return new ItemStack[]{new CaveStone().getItem(), Serum.getInstance().getItem()};
	}

	@Override
	public int getExp(AttackType type) {
		return 7;
	}
}
