package main.dungeon.contents.mob.zombie;

import java.util.ArrayList;

import main.common.event.player.PlayerCustomMobSpawnEvent;
import main.mob.mob.abstractmob.AbstractZombie;
import main.util.LivingEntityUtil;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ShyZombie extends AbstractZombie {

	@Override
	public String getName() {
		return "Shy Zombie";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		LivingEntity entity = e.getEntity();
		EntityEquipment equipment = entity.getEquipment();
		ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 4);
		equipment.setHelmet(itemStack);

		((Zombie)entity).setBaby(false);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(final LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		int nextInt = rnd.nextInt(3);
		if (nextInt == 0) {
			if (damager instanceof Player) {
				attack(mob, (Player) damager);
			} else {
				ArrayList<Player> nearByPlayer = LivingEntityUtil.getNearByPlayer(mob, 10, 4, 10);
				if (nearByPlayer.isEmpty()) {
					return;
				} else {
					final Player player = nearByPlayer.get(0);
					attack(mob, player);
				}
			}

		}
	}

	public void attack(final LivingEntity mob, final Player player) {
		Location location = player.getLocation();

		Location clone = location.clone();
		clone.subtract(location.getDirection().setY(0).normalize().multiply(0.4)).setY(location.getY());
		mob.teleport(clone);

		mob.getWorld().playSound(clone, Sound.ENDERMAN_TELEPORT, 1, 1);

		mob.getWorld().playSound(player.getLocation(), Sound.BLAZE_HIT, 1f, 0.5f);
		new ParticleData(ParticleType.angryVillager, 20).run(player.getLocation().add(0, 1, 0));;

		player.damage(0.0, mob);
		player.setHealth(Math.max(((Damageable)player).getHealth() - 4, 0));
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	@Override
	public boolean isAvoidPlayer() {
		return true;
	}

}
