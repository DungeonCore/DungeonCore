package main.dungeon.contents.mob.skelton;

import java.util.HashMap;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.common.other.Stun;
import main.mob.mob.abstractmob.AbstractSkelton;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class MineralizationArcher extends AbstractSkelton{
	HashMap<Player, Integer> mineralizationCountMap = new HashMap<Player, Integer>();

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		if (!(target instanceof Player)) {
			return;
		}

		final Player p = (Player) target;
		Integer integer = mineralizationCountMap.get(p);
		if (integer == null) {
			integer = 0;
		}
		int count = integer;
		count++;

		if (getCount(p) <= count) {
			Stun.addStun(p, (int) (20 * 2.5));
			count = 0;
		}
		mineralizationCountMap.put(p, count);
	}

	protected int getCount(Player p) {
		return 4;
	}

	@Override
	public String getName() {
		return "Mineralization Archer";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {

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
