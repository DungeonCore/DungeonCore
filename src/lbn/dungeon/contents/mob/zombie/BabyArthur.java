package lbn.dungeon.contents.mob.zombie;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.mob.abstractmob.AbstractZombie;
import lbn.util.LbnRunnable;
import lbn.util.LivingEntityUtil;

public class BabyArthur extends AbstractZombie{

	@Override
	public String getName() {
		return "Baby Arthur";
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		Zombie entity = (Zombie) e.getEntity();
		entity.setBaby(true);
		((Damageable)entity).setMaxHealth(25.0);
		((Damageable)entity).setHealth(25.0);
		LivingEntityUtil.setEquipment(entity, new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS), 0);
		LivingEntityUtil.setItemInHand(entity, new ItemStack(Material.DIAMOND_SWORD), 0);
	}



	public Zombie spawn(Location loc, final LivingEntity entity) {
		final Zombie spawn = spawn(loc);

		new LbnRunnable() {
			@Override
			public void run2() {
				if (!entity.isValid() || isElapsedSecond(300)) {
					spawn.remove();
					cancel();
				}
			}

			protected void runIfServerEnd() {
				spawn.remove();
			}
		}.runTaskTimer(20);

		return spawn;
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
