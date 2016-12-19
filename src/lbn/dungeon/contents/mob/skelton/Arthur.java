package lbn.dungeon.contents.mob.skelton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import lbn.chest.BossChest;
import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeon.contents.mob.zombie.BabyArthur;
import lbn.dungeoncore.Main;
import lbn.mob.LastDamageManager;
import lbn.mob.mob.BossMobable;
import lbn.mob.mob.abstractmob.AbstractSkelton;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.CircleParticleData;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

public class Arthur extends AbstractSkelton implements BossMobable{

	final public static double MAX_HEATH = 1000;

	@Override
	public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public String getName() {
		return "Arthur";
	}

	static Skeleton skelton;

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		final Skeleton entity = (Skeleton) e.getEntity();
		entity.getLocation().getWorld().playSound(e.getLocation(), Sound.WITHER_SPAWN, 1, 1);

		LivingEntityUtil.setEquipment(entity, new ItemStack[]{
				new ItemStack(Material.DIAMOND_HELMET),
				new ItemStack(Material.DIAMOND_CHESTPLATE),
				new ItemStack(Material.DIAMOND_LEGGINGS),
				new ItemStack(Material.DIAMOND_BOOTS)
		}, 0);

		LivingEntityUtil.setItemInHand(entity, new ItemStack(Material.DIAMOND_SWORD), 0);

		entity.setMaxHealth(MAX_HEATH);
		entity.setSkeletonType(SkeletonType.WITHER);

		skelton = entity;

		entity.setHealth(MAX_HEATH);

		new ArthurRoutine(skelton).runTaskTimer(Main.plugin, 0, 2);
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		if (((Damageable)mob).getHealth() > MAX_HEATH * 2.0 / 3.0) {
			e.setDamage(e.getDamage() + 0);
		} else if (((Damageable)mob).getHealth() > MAX_HEATH * 1.0 / 3.0) {
			e.setDamage(e.getDamage() + 10);
		} else if (((Damageable)mob).getHealth() >= 0) {
			e.setDamage(e.getDamage() + 20);
		}
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		Player p = LastDamageManager.getLastDamagePlayer(mob);
		if (!combatPlayerSet.contains(p)) {
			combatPlayerSet.add(p);
		}

		//全体ダメージ
		if (rnd.nextInt(6) == 0) {
			mob.getWorld().playSound(mob.getLocation(), Sound.WITHER_SHOOT, 1, (float) 0.1);
			ParticleData particleData = new ParticleData(ParticleType.cloud, 10).setDispersion(0.8, 0.8, 0.8);
//			CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.flame, 10), 6);
//			circleParticleData.run(mob.getLocation().add(0, 3, 0));
			for (Entity entity : mob.getNearbyEntities(6, 3, 6)) {
				if (LivingEntityUtil.isFriendship(entity)) {
					((LivingEntity)entity).damage(5.0);
					entity.setVelocity(new Vector(0, 0.5, 0));
					particleData.run(entity.getLocation().add(0, 2, 0));
				}
			}
			return;
		}

		//mob召喚
		if (((Damageable)mob).getHealth() > MAX_HEATH * 2.0 / 3.0) {
			if (rnd.nextInt(10) == 0) {
				spawnBabyArthur(mob.getLocation(), 1, mob);
			}
		} else if (((Damageable)mob).getHealth() > MAX_HEATH * 1.0 / 3.0) {
			if (rnd.nextInt(10) <= 1) {
				spawnBabyArthur(mob.getLocation(), 1, mob);
			}
		} else if (((Damageable)mob).getHealth() >= 0) {
			if (rnd.nextInt(10) <= 2) {
				spawnBabyArthur(mob.getLocation(), 2, mob);
			}
		}


	}

	private void spawnBabyArthur(Location location, int i, LivingEntity mob) {
		for (int c = 0; c < i; c++) {
			new BabyArthur().spawn(location, mob);
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
		skelton = null;
	}

	@Override
	public LivingEntity getEntity() {
		return skelton;
	}

	@Override
	public double getNoKnockback() {
		return 1;
	}

	@Override
	public BossChest getBossChest() {
		return null;
	}

	HashSet<Player> combatPlayerSet = new HashSet<Player>();

	@Override
	public Set<Player> getCombatPlayer() {
		return combatPlayerSet;
	}

	@Override
	public void setEntity(LivingEntity e) {
		if (e.getType() == getEntityType()) {
			skelton = (Skeleton) e;
		}
	}

}

class ArthurRoutine extends BukkitRunnable{
	Skeleton s;
	public ArthurRoutine(Skeleton skelton) {
		this.s = skelton;

		runTick = 10 * 30;
	}

	Random rnd = new Random();

	int runTick;

	int particleTerm = 10;

	@Override
	public void run() {
		//死んだら止める
		if (!s.isValid()) {
			cancel();
			return;
		}
		runTick--;
		particleTerm--;
		if (runTick <= 0) {
			runTick = 10 * 30;

			//HPが千以下の時に発動
			if (((Damageable)s).getHealth() > Arthur.MAX_HEATH * 1.0 / 3.0) {
				return;
			}

			doExcalibur();
		}

		//HPが少ない時はパーティクルを発生させる
		if (particleTerm <= 0) {
			//HPが千以下の時に発動
			if (((Damageable)s).getHealth() < Arthur.MAX_HEATH * 1.0 / 3.0) {
				new ParticleData(ParticleType.lava, 100).run(s.getLocation());
				particleTerm = 10;
			}
		}
	}

	private void doExcalibur() {
		//パーティクルを発生させる場所を取得する
		ArrayList<Player> nearByPlayer = LivingEntityUtil.getNearByPlayer(s, 30, 5, 30);
		final ArrayList<Location> particlePoint = new ArrayList<Location>();
		for (Player player : nearByPlayer) {
			particlePoint.add(player.getLocation());
			player.playSound(player.getLocation(), Sound.HORSE_SKELETON_DEATH, 1, (float) 0.5);
		}

		//10秒間の間、パーティクルを発生させる
		final CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.portal, 10).setDispersion(0, 0, 0), 1.5);
		new BukkitRunnable() {
			int count = 0;
			@Override
			public void run() {
				circleParticleData.run(particlePoint);
				count++;
				if (!s.isValid() || count >= 10) {
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 20);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!s.isValid()) {
					return;
				}

				s.getWorld().playSound(s.getLocation(), Sound.EXPLODE, 1, (float) 0.5);
				ArrayList<Player> nearByPlayer = LivingEntityUtil.getNearByPlayer(s, 20, 5, 20);
				for (Location location : particlePoint) {
					for (Player player : nearByPlayer) {
						if (player.getLocation().distance(location) <= 1.6) {
							player.damage(0.0, s);

							if (player.getGameMode() != GameMode.CREATIVE) {
								player.setHealth(Math.max(((Damageable)player).getHealth() - 15, 0));
							}
							continue;
						}
					}
					//落雷を落とす
					LivingEntityUtil.strikeLightningEffect(location, nearByPlayer);
				}
				new ParticleData(ParticleType.lava, 100).run(particlePoint);;
			}
		}.runTaskLater(Main.plugin, 20 * 10);
	}


}
