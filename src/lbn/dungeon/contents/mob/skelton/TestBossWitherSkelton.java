package lbn.dungeon.contents.mob.skelton;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lbn.chest.BossChest;
import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.LastDamageManager;
import lbn.mob.mob.BossMobable;
import lbn.mob.mob.abstractmob.AbstractSkelton;
import lbn.util.LivingEntityUtil;

public class TestBossWitherSkelton extends AbstractSkelton implements BossMobable{

	@Override
	public String getName() {
		return "TEST BOSS";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.WITHER_SPAWN, 1, 1);

		Skeleton entity = (Skeleton) e.getEntity();
		LivingEntityUtil.setEquipment(entity, new ItemStack[]{
				new ItemStack(Material.DIAMOND_HELMET),
				new ItemStack(Material.DIAMOND_CHESTPLATE),
				new ItemStack(Material.DIAMOND_LEGGINGS),
				new ItemStack(Material.DIAMOND_BOOTS)
		}, 0);
		LivingEntityUtil.setItemInHand(entity, new ItemStack(Material.DIAMOND_SWORD), 0);

		entity.setMaxHealth(100.0);
		entity.setSkeletonType(SkeletonType.WITHER);

		s = entity;
	}

	Skeleton s;

	@Override
	public LivingEntity getEntity() {
		return s;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		target.setNoDamageTicks(0);
		if (rnd.nextInt(5) == 0) {
			target.setVelocity(new Vector(0, 2, 0));
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() / 3.0);

		Player p = LastDamageManager.getLastDamagePlayer(mob);
		if (!combatPlayerSet.contains(p)) {
			combatPlayerSet.add(p);
		}
	}

	HashSet<Player> combatPlayerSet = new HashSet<Player>();

	@Override
	public Set<Player> getCombatPlayer() {
		return combatPlayerSet;
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public double getNoKnockback() {
		return 1;
	}

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {

	}

	@Override
	public BossChest getBossChest() {
		return null;
	}

	@Override
	public void setEntity(LivingEntity e) {
		if (e.getType() == getEntityType()) {
			s = (Skeleton) e;
		}
	}

}
