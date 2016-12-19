package main.mob.mob.abstractmob;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.AbstractMob;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class AbstractCombinationMob<K extends LivingEntity> extends AbstractMob<K>{

	@Override
	public boolean isRiding() {
		return true;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}

	abstract protected void onSpawn(LivingEntity spawnMob);

	abstract protected AbstractMob<?> getBaseMob();

	@Override
	public EntityType getEntityType() {
		return getBaseMob().getEntityType();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected K spawnPrivate(Location loc) {
		Entity spawnPrivate = getBaseMob().spawn(loc);
		onSpawn((LivingEntity)spawnPrivate);
//		spawnPrivate.setCustomName(getBaseMob().getName());
		return (K) spawnPrivate;
	}

	abstract public AbstractMob<?>[] getCombinationMobListForSpawn();

}
