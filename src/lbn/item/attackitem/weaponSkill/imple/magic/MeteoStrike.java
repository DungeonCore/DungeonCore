package lbn.item.attackitem.weaponSkill.imple.magic;

import java.util.List;

import lbn.dungeoncore.Main;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.player.ItemType;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;
import lbn.util.dropingEntity.DropingEntityForPlayer;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MeteoStrike extends WeaponSkillForOneType{

	public MeteoStrike() {
		super(ItemType.MAGIC);
	}

	@Override
	public String getId() {
		return "skill3";
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		new DropItemImplement(p, customItem).runTaskTimer();
		return true;
	}

	public void executeMeteo(Entity centerEntity, AbstractAttackItem customItem) {
		//落下地点
		final Location target = centerEntity.getLocation();

		//開始地点
		final Location start = target.clone().add(3, 30, 0);

		//方向ベクトル
		Vector direction = target.clone().subtract(start).toVector().normalize();

		//1tikc移動距離
		final double stepSize = target.clone().distance(start) / (20 * 2);

		//音を鳴らす
		JavaUtil.sendSound(target, Sound.GHAST_FIREBALL, 3f, 0.2f, 50);

		new BukkitRunnable() {
			Location nowLocation = start.clone();
			Location beforeLocation = start.clone();

			ParticleData nowData = new ParticleData(ParticleType.lava, 200).setDispersion(0.8, 1, 0.8).setFarParticle(true);
			ParticleData fireData = new ParticleData(ParticleType.largesmoke, 70).setDispersion(0.2, 0.2, 0.2).setFarParticle(true);
			ParticleData beforeData = new ParticleData(ParticleType.lava, 70).setDispersion(0.5, 1, 0.5).setFarParticle(true);

			int time = 0;
			@Override
			public void run() {
				nowData.run(nowLocation);
				beforeData.run(beforeLocation.add(direction.clone().multiply(stepSize * -3)));
				fireData.run(nowLocation);

				beforeLocation = nowLocation.clone();
				//隕石を移動させる
				nowLocation = nowLocation.add(direction.clone().multiply(stepSize));

				time++;
				if (time >= 20 * 2) {
					//爆発を起こす
					explode(customItem, centerEntity);
					cancel();
					//アイテムを消す
					centerEntity.remove();
				}
			}
			/**
			 * 爆発をおこしダメージを与える
			 * @param e
			 * @param centerEntity
			 */
			protected void explode(AbstractAttackItem item, Entity centerEntity) {
				//爆発パーティクル
				new ParticleData(ParticleType.hugeexplosion, 10).setFarParticle(true).run(target);
				//爆発音
				JavaUtil.sendSound(target, Sound.EXPLODE, 3f, 0.1f, 50);

				//敵にダメージを与え吹っ飛ばす
				List<LivingEntity> nearEmemy = LivingEntityUtil.getNearEnemy(centerEntity, 15, 15, 15);
				for (LivingEntity entity : nearEmemy) {
					//ボスで無いならEntityを吹っ飛ばす
					AbstractMob<?> mob = MobHolder.getMob(entity);
					if (!mob.isBoss()) {
						entity.setVelocity(getMoveVector(entity, entity, 2));
					}
					//2倍のダメージを与える
					entity.damage(item.getAttackItemDamage(0)* getData(0), entity);
					//15秒燃やす
					entity.setFireTicks((int) (20 * getData(1)));
				}
			}

		}.runTaskTimer(Main.plugin, 0, 1);
	}

	public static Vector getMoveVector(Entity center, Entity target, double power) {
		return target.getLocation().toVector().subtract(center.getLocation().toVector()).normalize().multiply(power).setY(1);
	}

	class DropItemImplement extends DropingEntityForPlayer {

		AbstractAttackItem customItem;
		public DropItemImplement(Player p, AbstractAttackItem customItem) {
			super(p.getLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(0.8), p.getLocation(), Material.NETHER_STAR, (byte) 0);
			this.customItem = customItem;
		}

		ParticleData particleDataLava = new ParticleData(ParticleType.lava, 5);

		@Override
		public void onGround(Entity spawnEntity) {
			executeMeteo(spawnEntity, customItem);
		}

		@Override
		public void tickRutine(int count) {
			super.tickRutine(count);

			if (count % 4 == 0) {
				particleDataLava.run(spawnEntity.getLocation());
			}
		}

	}

}
