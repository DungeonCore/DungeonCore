package lbn.mob.mob.abstractmob.villager;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NullVillager extends AbstractVillager{


	Location loc;
	public NullVillager(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}

	@Override
	protected List<String> getMessage(Player p, Villager mob) {
		return null;
	}
	String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
	}

}
