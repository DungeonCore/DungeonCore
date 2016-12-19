package lbn.dungeon.contents.mob.witch;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.chest.BossChest;
import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeon.contents.mob.zombie.NecroZombie;
import lbn.mob.LastDamageManager;
import lbn.mob.mob.BossMobable;
import lbn.mob.mob.abstractmob.AbstractWitch;

//http://prnt.sc/bw4jm4
public class Necromancer extends AbstractWitch implements BossMobable{

	protected double getMaxHealth() {
		return 30000;
	}

	@Override
	public String getName() {
		return "Necromancer";
	}

	Witch w;

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();

		w = (Witch) entity;

		entity.setMaxHealth(getMaxHealth());

		EntityEquipment equipment = entity.getEquipment();
		ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
		itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		equipment.setItemInHand(itemStack);
		equipment.setItemInHandDropChance(0);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {

	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
		w = null;
	}

	@Override
	public LivingEntity getEntity() {
		return w;
	}

	public void addHealth(double val) {
		if (w != null || w.isValid()) {
			w.setHealth(((Damageable)w).getHealth() + val);
		}
	}

	@Override
	public BossChest getBossChest() {
		return null;
	}

	HashSet<Player> combatPlayerSet = new HashSet<Player>();

	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		Player p = LastDamageManager.getLastDamagePlayer(mob);
		if (!combatPlayerSet.contains(p)) {
			combatPlayerSet.add(p);
		}
	}

	@Override
	public Set<Player> getCombatPlayer() {
		return combatPlayerSet;
	}

	@Override
	public void setEntity(LivingEntity e) {
		if (e.getType() == getEntityType()) {
			w = (Witch) e;
		}
	}

}

class NecromancerRoutine extends BukkitRunnable {
	public NecromancerRoutine(Necromancer necromancer) {
		this.witch = (Witch) necromancer.getEntity();
		this.necromancer = necromancer;
		this.necroZombie = new NecroZombie();
	}

	Witch witch;
	Necromancer necromancer;

	NecroZombie necroZombie;

	@Override
	public void run() {
		if (!witch.isValid()) {
			cancel();
		}

		double health = ((Damageable)witch).getHealth();
		double maxHealth = ((Damageable)witch).getMaxHealth();

		if (health > maxHealth / 2.0) {

		} else if (health > maxHealth / 3.0) {

		} else if (health > maxHealth / 10.0) {

		} else {
			necroZombie.spawn(witch.getLocation(), necromancer);
			necroZombie.spawn(witch.getLocation(), necromancer);
			necroZombie.spawn(witch.getLocation(), necromancer);
		}

	}

}
