package lbn.mob.mobskill;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.mob.BossMobable;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;
import lbn.util.damagedFalling.DamageFallingblockForMonsterSkill;
import lbn.util.particle.ParticleData;

public class NormalMobSkill implements MobSkillInterface{

	public NormalMobSkill(PotionEffect potionEffect, double damage,
			int fireTick, MobSkillRunnable runnable,
			MobSkillExcuteTimingType timing,
			MobSkillExcuteConditionType condition, String id, int percent, ParticleData particleData,
			ParticleLocationType particleLocType, MobSkillTargetingMethodType targetingMethod,
			String targetingMethodData, int laterTick) {
		this.potionEffect = potionEffect;
		this.damage = damage;
		this.fireTick = fireTick;
		this.runnable = runnable;
		this.timing = timing;
		this.condition = condition;
		this.id = id;
		this.percent = percent;
		this.particleData = particleData;
		this.particleLocType = particleLocType;
		this.targetingMethod = targetingMethod;
		this.targetingDeta = targetingMethodData;
		this.laterTick = laterTick;
	}

	protected MobSkillTargetingMethodType targetingMethod;
	protected String targetingDeta = null;
	protected PotionEffect potionEffect;
	protected double damage;
	protected int fireTick;
	protected MobSkillRunnable runnable;
	private MobSkillExcuteTimingType timing;
	private MobSkillExcuteConditionType condition;
	private String id;
	private int percent;
	int laterTick;
	ParticleData particleData;
	ParticleLocationType particleLocType;

	@Override
	public void execute(LivingEntity condtionTarget, LivingEntity mob) {
		if (mob == null) {
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {

				if (targetingMethod == MobSkillTargetingMethodType.DROPING_ITEM || targetingMethod == MobSkillTargetingMethodType.FALLING_BLOCK) {
					executeFallingblockDamage(condtionTarget, mob);
				} else {
					executeDamageOther(condtionTarget, mob);
				}

			}
		}.runTaskLater(Main.plugin, laterTick);
	}

	protected void executeDamageOther(LivingEntity condtionTarget, LivingEntity mob) {
		AbstractMob<?> mob2 = MobHolder.getMob(mob);
		if (mob2 == null || mob2.isNullMob()) {
			return;
		}

		ArrayList<LivingEntity> targetList = new ArrayList<LivingEntity>();
		switch (targetingMethod) {
		case DEPEND_ON_CONDTION:
			if (condtionTarget != null && condtionTarget.isValid()) {
				targetList.add(condtionTarget);
			}
			break;
		case RANGE_BY_CONDTION_TARGET:
			//もしターゲットがいないなら不発
			if (condtionTarget == null || !condtionTarget.isValid()) {
				break;
			}
			double range = JavaUtil.getDouble(targetingDeta, 5);
			//周囲のプレイヤーを取得
			for (Entity entity : condtionTarget.getNearbyEntities(range, 3, range)) {
				//プレイヤーなどでないなら無視
				if (!LivingEntityUtil.isFriendship(entity)) {
					continue;
				}
				//bossの時, combatプレイヤーでないなら無視
				if (entity.getType() == EntityType.PLAYER && mob2.isBoss()) {
					if (!((BossMobable)mob2).getCombatPlayer().contains(entity)) {
						continue;
					}
				}
				targetList.add((LivingEntity) entity);
			}
			//対象のプレイヤーも入れる
			targetList.add(condtionTarget);
			break;
		case RANGE_BY_MOB:
			//モブの周囲のプレイヤーを取得
			double range2 = JavaUtil.getDouble(targetingDeta, 5);
			for (Entity entity : mob.getNearbyEntities(range2, 3, range2)) {
				//プレイヤーなどでないなら無視
				if (!LivingEntityUtil.isFriendship(entity)) {
					continue;
				}
				//bossの時, combatプレイヤーでないなら無視
				if (entity.getType() == EntityType.PLAYER && mob2.isBoss()) {
					if (!((BossMobable)mob2).getCombatPlayer().contains(entity)) {
						continue;
					}
				}
				targetList.add((LivingEntity) entity);
			}
			break;
		default:
			targetList.add(condtionTarget);
			break;
		}

		for (LivingEntity livingEntity : targetList) {
			executeOneTarget(livingEntity, mob);
		}

		//パーティクルを発動
		executeParticle(targetList, mob);
	}

	protected void executeFallingblockDamage(LivingEntity condtionTarget, LivingEntity mob) {
		int blockId = 3;
		int data = 0;
		double speed = 2.0;

		if (targetingDeta == null) {
			targetingDeta = "";
		}
		String[] split = targetingDeta.split(",");
		if (split.length >= 1) {
			String[] split2 = split[0].split(":");
			if (split2.length >= 1) {
				blockId = JavaUtil.getInt(split2[0], 3);
			}
			if (split2.length >= 2) {
				data = JavaUtil.getInt(split2[1], 0);
			}
		}
		if (split.length >= 2) {
			speed = JavaUtil.getDouble(split[1], 2);
		}

		DamageFallingblockForMonsterSkill damageFallingblockForMonsterSkill = new DamageFallingblockForMonsterSkill(mob, condtionTarget.getLocation(), getMaterialById(blockId), (byte)data, speed){
			ArrayList<LivingEntity> damagedList = new ArrayList<>();
			@Override
			protected void executeDamage(LivingEntity target, LivingEntity mob) {
				executeOneTarget(target, mob);
				damagedList.add(target);
			}
			@Override
			public synchronized void cancel() throws IllegalStateException {
				super.cancel();
				executeParticle(damagedList, mob);
			}
		};
		damageFallingblockForMonsterSkill.runTaskTimer();
	}

	protected Material getMaterialById(int blockDeta) {
		@SuppressWarnings("deprecation")
		Material material = Material.getMaterial(blockDeta);
		if (material == null) {
			material = Material.OBSIDIAN;
		}
		return material;
	}

	protected void executeParticle(ArrayList<LivingEntity> targetList, LivingEntity mob) {
		if (particleData != null) {
			//軽量化のため、順次実行
			new BukkitRunnable() {
				int i = 0;
				@Override
				public void run() {
					if (i >= targetList.size()) {
						cancel();
						return;
					}
					particleData.run(particleLocType.getLocation(mob, targetList.get(i)));
					i++;
				}
			}.runTaskTimer(Main.plugin, 0, 1);
		}
	}

	protected void executeOneTarget(LivingEntity condtionTarget, LivingEntity mob) {
		if (potionEffect != null) {
			condtionTarget.addPotionEffect(potionEffect);
		}

		if (damage > 0) {
			condtionTarget.damage(damage);
		}

		if (fireTick > 0) {
			condtionTarget.setFireTicks(fireTick);
		}

		runnable.execute(condtionTarget, mob);
	}

	@Override
	public MobSkillExcuteTimingType getTiming() {
		return timing;
	}

	@Override
	public MobSkillExcuteConditionType getCondtion() {
		return condition;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public int excutePercent() {
		return percent;
	}

	@Override
	public MobSkillTargetingMethodType getTargetingMethod() {
		return targetingMethod;
	}
}
