package lbn.dungeon.contents.mob.skelton;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeon.contents.item.shootbow.BowOfExplosion;
import lbn.item.strength.StrengthOperator;
import lbn.mob.mob.abstractmob.AbstractSkelton;

public class ExplosionSkeleton extends AbstractSkelton {

	@Override
	public String getName() {
		return "Explosion Skeleton";
	}

	static BowOfExplosion bow = new BowOfExplosion();


	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		EntityEquipment equipment = entity.getEquipment();
		ItemStack item = bow.getItem();
		StrengthOperator.updateLore(item, rnd.nextInt(4));
		equipment.setItemInHand(item);
		equipment.setItemInHandDropChance(0);
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

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
