package lbn.dungeon.contents.mob.zombie;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeon.contents.mob.witch.Necromancer;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

public class NecroZombie extends AbstractZombie{

	@Override
	public String getName() {
		return "NecroZombie";
	}

	static HashMap<Zombie, Necromancer> mobMap = new HashMap<Zombie, Necromancer>();

	public Zombie spawn(Location loc, Necromancer necro) {
		Zombie spawn = spawn(loc);
		LivingEntityUtil.removeEquipment(spawn);
		mobMap.put(spawn, necro);
		return spawn;
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		e.setDamage(10.0);
		Necromancer necromancer = mobMap.get(mob);
		if (necromancer != null) {
			necromancer.addHealth(5);
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
