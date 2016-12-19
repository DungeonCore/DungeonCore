package main.dungeon.contents.mob.skelton;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.dungeon.contents.item.material.CaveStone;
import main.dungeon.contents.item.questItem.Serum;
import main.mob.mob.abstractmob.AbstractSkelton;
import main.player.AttackType;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CaveSkeleton extends AbstractSkelton{
	@Override
	public String getName() {
		return "Cave Skeleton";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		entity.setMaxHealth(10.0);
		entity.setHealth(10.0);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		if (rnd.nextInt(2) == 0) {
			if (mob.getLocation().distance(damager.getLocation()) < 4) {
				Vector vectorMob = mob.getLocation().toVector();
				Vector vectorDamager = damager.getLocation().toVector();
				damager.setVelocity(vectorDamager.subtract(vectorMob).normalize().multiply(3).setY(0));
			}
		}

	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * 0.6);
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
