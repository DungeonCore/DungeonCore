package net.l_bulb.dungeoncore.mob.mobskill;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.common.buff.BuffData;
import net.l_bulb.dungeoncore.common.buff.BuffDataFactory;
import net.l_bulb.dungeoncore.common.dropingEntity.DamageFallingblockForMonsterSkill;
import net.l_bulb.dungeoncore.common.particle.ParticleManager;
import net.l_bulb.dungeoncore.common.sound.SoundData;
import net.l_bulb.dungeoncore.common.sound.SoundManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.customMob.BossMobable;
import net.l_bulb.dungeoncore.mob.customMob.SpreadSheetMob;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class OldNormalMobSkill implements MobSkillInterface {

  public OldNormalMobSkill(double damage, int fireTick,
      MobSkillRunnable runnable, MobSkillExcuteTimingType timing,
      MobSkillExcuteConditionType condition,
      String id, int percent, String particleId, ParticleLocationType particleLocType,
      MobSkillTargetingMethodType targetingMethod, String targetingMethodData,
      int laterTick, String chainId, String skillTalk, String soundId, boolean isSoundTargetOnePlayer, String buffId1, boolean isTargetMobBuff) {
    this.damage = damage;
    this.fireTick = fireTick;
    this.runnable = runnable;
    this.timing = timing;
    this.condition = condition;
    this.id = id;
    this.percent = percent;
    this.particleId = particleId;
    this.particleLocType = particleLocType;
    this.targetingMethod = targetingMethod;
    this.targetingDeta = targetingMethodData;
    this.laterTick = laterTick;
    this.chainId = chainId;
    this.skillTalk = skillTalk;
    this.soundId = soundId;
    this.isSoundTargetOnePlayer = isSoundTargetOnePlayer;
    this.buffId1 = buffId1;
    this.isTargetMobBuff1 = isTargetMobBuff;
  }

  protected MobSkillTargetingMethodType targetingMethod;
  protected String targetingDeta = null;
  protected double damage;
  protected int fireTick;
  protected MobSkillRunnable runnable;
  private MobSkillExcuteTimingType timing;
  private MobSkillExcuteConditionType condition;
  private String id;
  private int percent;
  int laterTick;
  String particleId;
  ParticleLocationType particleLocType;
  String chainId;
  String skillTalk;
  String soundId;
  boolean isSoundTargetOnePlayer;

  String buffId1;
  boolean isTargetMobBuff1 = true;

  @Override
  public void execute(Entity condtionTarget, Entity mob) {
    if (mob == null) { return; }

    if (laterTick == 0) {
      executeMobSkill(condtionTarget, mob);
    } else {
      new BukkitRunnable() {
        @Override
        public void run() {
          executeMobSkill(condtionTarget, mob);
        }
      }.runTaskLater(Main.plugin, laterTick);
    }
  }

  public void executeMobSkill(Entity condtionTarget, Entity mob) {
    // 対象の選び方によって実行方法を変える
    if (targetingMethod == MobSkillTargetingMethodType.DROPING_ITEM || targetingMethod == MobSkillTargetingMethodType.FALLING_BLOCK) {
      executeFallingblockDamage(condtionTarget, mob);
    } else {
      executeDamageOther(condtionTarget, mob);
    }
  }

  /**
   * 指定されたターゲット方法でターゲットを取り、スキルを実行
   *
   * @param condtionTarget
   * @param mob
   */
  protected void executeDamageOther(Entity condtionTarget, Entity mob) {
    AbstractMob<?> mob2 = MobHolder.getMob(mob);
    if (mob2 == null || mob2.isNullMob()) { return; }

    ArrayList<Entity> targetList = new ArrayList<>();
    switch (targetingMethod) {
      case DEPEND_ON_CONDTION:
        if (condtionTarget != null && condtionTarget.isValid()) {
          targetList.add(condtionTarget);
        }
        break;
      case RANGE_BY_CONDTION_TARGET:
        // もしターゲットがいないなら不発
        if (condtionTarget == null || !condtionTarget.isValid()) {
          break;
        }
        double range = JavaUtil.getDouble(targetingDeta, 5);
        // 周囲のプレイヤーを取得
        for (Entity entity : condtionTarget.getNearbyEntities(range, 3, range)) {
          // プレイヤーなどでないなら無視
          if (!LivingEntityUtil.isFriendship(entity)) {
            continue;
          }
          // bossの時, combatプレイヤーでないなら無視
          if (entity.getType() == EntityType.PLAYER && mob2.isBoss()) {
            if (!((BossMobable) mob2).getCombatPlayer().contains(entity)) {
              continue;
            }
          }
          targetList.add(entity);
        }
        // 対象のプレイヤーも入れる
        targetList.add(condtionTarget);
        break;
      case RANGE_BY_MOB:
        // モブの周囲のプレイヤーを取得
        double range2 = JavaUtil.getDouble(targetingDeta, 5);
        for (Entity entity : mob.getNearbyEntities(range2, 3, range2)) {
          // プレイヤーなどでないなら無視
          if (!LivingEntityUtil.isFriendship(entity)) {
            continue;
          }
          // bossの時, combatプレイヤーでないなら無視
          if (entity.getType() == EntityType.PLAYER && mob2.isBoss()) {
            if (!((BossMobable) mob2).getCombatPlayer().contains(entity)) {
              continue;
            }
          }
          targetList.add(entity);
        }
        break;
      default:
        targetList.add(condtionTarget);
        break;
    }

    for (Entity livingEntity : targetList) {
      executeOneTarget(livingEntity, mob);
    }

    // パーティクルを発動
    executeAfter(targetList, mob);
  }

  /**
   * 対象にブロックかアイテムを当ててスキルを実行
   *
   * @param condtionTarget
   * @param mob
   */
  protected void executeFallingblockDamage(Entity condtionTarget, Entity mob) {
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

    DamageFallingblockForMonsterSkill damageFallingblockForMonsterSkill = new DamageFallingblockForMonsterSkill(mob, condtionTarget.getLocation(),
        getMaterialById(blockId), (byte) data, speed) {
      ArrayList<Entity> damagedList = new ArrayList<>();

      @Override
      protected void executeDamage(LivingEntity target, LivingEntity mob) {
        executeOneTarget(target, mob);
        damagedList.add(target);
      }

      @Override
      public synchronized void cancel() throws IllegalStateException {
        super.cancel();

        if (damagedList.size() != 0) {
          executeAfter(damagedList, mob);
        }
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

  /**
   * スキル発動成功時、一回だけ実行される
   *
   * @param targetList
   * @param mob
   */
  protected void executeAfter(ArrayList<Entity> targetList, Entity mob) {
    // パーティクルを実行
    MobSkillUtil.playParticle(ParticleManager.getParticleData(particleId), targetList, mob, particleLocType);

    // 周囲に対して実行の場合はここで一回だけ音を鳴らす
    if (!isSoundTargetOnePlayer) {
      SoundData fromId = SoundManager.fromId(soundId);
      if (fromId != null) {
        fromId.playSoundAllPlayer(mob.getLocation());
      }
    }

    // バフのターゲットがモンスターの場合はここで実行
    if (isTargetMobBuff1 && mob.getType().isAlive()) {
      BuffData buffFromId = BuffDataFactory.getBuffFromId(buffId1);
      if (buffFromId != null) {
        buffFromId.addBuff((LivingEntity) mob);
      }
    }
  }

  /**
   * 一人のターゲットに対してスキルを即時実行
   *
   * @param condtionTarget
   * @param mob
   */
  protected void executeOneTarget(Entity condtionTarget, Entity mob) {
    if (fireTick > 0) {
      condtionTarget.setFireTicks(fireTick);
    }

    // ターゲットがPlayerのときはバフ効果を発動する
    if (condtionTarget.getType().isAlive()) {
      if (!isTargetMobBuff1) {
        BuffData buff1 = BuffDataFactory.getBuffFromId(buffId1);
        if (buff1 != null) {
          buff1.addBuff((LivingEntity) condtionTarget);
        }
      }

      if (damage > 0) {
        ((Damageable) condtionTarget).damage(damage);
      }
    }
    runnable.execute(condtionTarget, mob);

    // skilltalkが設定されている場合は実行
    if (skillTalk != null && !skillTalk.isEmpty()) {
      if (condtionTarget.getType() == EntityType.PLAYER) {
        ((Player) condtionTarget).sendMessage(skillTalk);
      }
    }

    // 対象に対してのみ実行の場合はここで一回だけ音を鳴らす
    if (isSoundTargetOnePlayer) {
      if (condtionTarget.getType() == EntityType.PLAYER) {
        SoundData fromId = SoundManager.fromId(soundId);
        if (fromId != null) {
          fromId.playSoundOnePlayer(mob.getLocation(), (Player) condtionTarget);
        }
      }
    }

    // 次のスキルが設定されている場合は実行
    if (chainId != null && !chainId.equals(id)) {
      MobSkillInterface fromId = MobSkillManager.fromId(chainId);
      if (fromId != null) {
        SpreadSheetMob.MobSkillExecutor(mob, condtionTarget, MobSkillManager.fromId(chainId));
      }
    }
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
