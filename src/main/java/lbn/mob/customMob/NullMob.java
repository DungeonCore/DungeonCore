package lbn.mob.customMob;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.AbstractMob;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NullMob extends AbstractMob<LivingEntity>{

	public NullMob() {
		this("");
	}

	String name;

	public NullMob(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}


	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public boolean isNullMob() {
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
	public LivingEntity spawnPrivate(Location loc) {
		return null;
	}

	@Override
	public EntityType getEntityType() {
		return null;
	}

}
