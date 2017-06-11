package net.l_bulb.dungeoncore.mob.customMob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AttackDamageValue;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.MobSpawnerFromCommand;
import net.l_bulb.dungeoncore.mob.SummonPlayerManager;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillExcuteConditionType;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillExecutor;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillInterface;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillManager;
import net.l_bulb.dungeoncore.player.ExpTable;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.BlockUtil;

public class SpreadSheetMob extends AbstractMob<Entity> {

  protected SpreadSheetMob(LbnMobTag nbtTag, String[] command, String name) {
    this.command = command;
    this.nbtTag = nbtTag;
    this.name = name;
    isRiding = nbtTag.isRiding();
  }

  boolean isRiding = false;

  @Override
  public boolean isRiding() {
    return isRiding;
  }

  @Override
  public LbnMobTag getLbnMobTag() {
    return nbtTag;
  }

  public static SpreadSheetMob getInstance(String[] command, String name, CommandSender sender) {
    LbnMobTag nbtTag = MobSpawnerFromCommand.getNBTTagByCommand(command, sender);

    if (nbtTag == null) { return null; }

    SpreadSheetMob commandableMob = new SpreadSheetMob(nbtTag, command, name);

    nbtTag.setBoss(false);

    AbstractMob<?> mob2 = MobHolder.getMob(name);
    if (mob2 != null && !mob2.isNullMob() && !(mob2 instanceof SpreadSheetMob)) {
      commandableMob.mob = mob2;
    }
    return commandableMob;
  }

  AbstractMob<?> mob = null;

  String name;

  String[] command;

  LbnMobTag nbtTag;

  @Override
  @EventHandler
  protected Entity spawnPrivate(Location loc) {
    Entity spawn = MobSpawnerFromCommand.spawn(loc, command, nbtTag);

    if (nbtTag.isAutoFixHp() && nbtTag.getLevel() >= 0) {
      if (spawn.getType().isAlive()) {
        double oldHp = ((LivingEntity) spawn).getMaxHealth();
        ((LivingEntity) spawn).setMaxHealth(
            AttackDamageValue.getAttackDamageValue(AttackDamageValue.getCombatLoad(nbtTag.getLevel(), ItemType.SWORD), nbtTag.getLevel())
                / AttackDamageValue.getAttackDamageValue(AttackDamageValue.getCombatLoad(9, ItemType.SWORD), 9) * oldHp);
        ((LivingEntity) spawn).setHealth(((LivingEntity) spawn).getMaxHealth());
      }
    }

    return spawn;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    if (redstone != null) {
      redstone.getBlock().setType(Material.AIR);
    }

    // mobskill発動
    executeMobSkill(e.getEntity(), null, MobSkillExcuteConditionType.MOB_SPAWN);

    if (this.mob == null) { return; }
    this.mob.onSpawn(e);

  }

  @Override
  public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {
    // mobskill
    if (target.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(target)) {
      executeMobSkill(mob, target, MobSkillExcuteConditionType.MOB_ATTACK);
    }
    super.onProjectileHitEntity(mob, target, e);

    // 攻撃ポイント
    e.setDamage(e.getDamage() * attackPoint);
  }

  double attackValue = 0;

  public void setAttackValue(double attackValue) {
    this.attackValue = attackValue;
  }

  public double getAttackValue() {
    return attackValue;
  }

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {
    if (mob == target) {
      e.setCancelled(true);
      return;
    }

    if (getAttackValue() >= 0) {
      if (e.isApplicable(DamageModifier.BASE)) {
        e.setDamage(DamageModifier.BASE, getAttackValue());
      }
    }

    // skill
    executeMobSkill(mob, target, MobSkillExcuteConditionType.MOB_ATTACK);

    // 攻撃力ポイント
    e.setDamage(e.getDamage() * attackPoint);

    // もとのmobの効果
    if (this.mob == null) { return; }
    this.mob.onAttack(mob, target, e);
  }

  @Override
  public void onDamagePlayer(CombatEntityEvent e) {
    switch (e.geItemType()) {
      case SWORD:
        if (swordRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - swordRegistance / 100.0));
        break;
      case MAGIC:
        if (magicRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - magicRegistance / 100.0));
        break;
      case BOW:
        if (bowRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - bowRegistance / 100.0));
        break;
      default:
        break;
    }
  }

  @Override
  public void onDamage(LivingEntity mob, Entity damager,
      EntityDamageByEntityEvent e) {
    if (mob == damager) {
      e.setCancelled(true);
      return;
    }

    // skill
    if (damager.getType().isAlive()) {
      executeMobSkill(mob, (LivingEntity) damager, MobSkillExcuteConditionType.MOB_DAMAGED);
    } else if (damager.getType() == EntityType.ARROW) {
      ProjectileSource shooter = ((Arrow) damager).getShooter();
      if (shooter instanceof Player) {
        executeMobSkill(mob, (LivingEntity) shooter, MobSkillExcuteConditionType.MOB_DAMAGED);
      }
    }

    // 耐性
    switch (LastDamageManager.getLastDamageAttackType(mob)) {
      case SWORD:
        if (swordRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - swordRegistance / 100.0));
        break;
      case MAGIC:
        if (magicRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - magicRegistance / 100.0));
        break;
      case BOW:
        if (bowRegistance == 100) {
          e.setCancelled(true);
        }
        e.setDamage(e.getDamage() * (1 - bowRegistance / 100.0));
        break;
      default:
        break;
    }

    // 防御ポイント
    e.setDamage(e.getDamage() * defencePoint);

    if (this.mob == null) { return; }
    this.mob.onDamage(mob, damager, e);

  }

  @Override
  public void onOtherDamage(EntityDamageEvent e) {
    if (e.getCause() == DamageCause.DROWNING && nbtTag.isWaterMonster()) {
      e.setCancelled(true);
    }

    if (this.mob != null) {
      this.mob.onOtherDamage(e);
    }
  }

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {
    // RSを設置
    if (redstone != null) {
      BlockUtil.setRedstone(redstone);
    }

    // mobskill発動
    executeMobSkill(e.getEntity(), e.getEntity().getKiller(), MobSkillExcuteConditionType.MOB_DEATH);

    if (this.mob == null) { return; }
    this.mob.onDeathPrivate(e);
  }

  @Override
  public EntityType getEntityType() {
    return nbtTag.getType();
  }

  private int index = 0;

  private double[] dropPerList = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
  private ItemStack[] dropItemList = { null, null, null, null, null, null, null, null, null, null };

  public void setDropItem(ItemStack item, double dropPer) {
    dropPerList[index] = dropPer;
    dropItemList[index] = item;
    index++;
  }

  @Override
  public List<ItemStack> getDropItem(Player lastDamagePlayer) {
    List<ItemStack> dropItem = new ArrayList<>();
    if (this.mob != null) {
      dropItem.addAll(this.mob.getDropItem(lastDamagePlayer));
    }
    Random random = new Random();
    for (int i = 0; i < index; i++) {
      // 確率を調べる
      if (random.nextInt(1000) < dropPerList[i] * 10) {
        dropItem.add(dropItemList[i]);
      }
    }
    // 関係のないクエストのアイテムを削除する
    removeOtherQuestItem(lastDamagePlayer, dropItem);
    return dropItem;
  }

  /**
   * ターゲット時
   */
  @Override
  public void onTarget(EntityTargetLivingEntityEvent event) {
    LivingEntity target = event.getTarget();
    if (event.getEntity().getType() != EntityType.GUARDIAN) {
      if (target == null || target == event.getEntity()) {
        event.setCancelled(true);
        return;
      }
    }

    if (target != null && (target.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(target))) {
      executeMobSkill((LivingEntity) event.getEntity(), event.getTarget(), MobSkillExcuteConditionType.TARGET_PLAYER);
    }
  }

  HashSet<String> skillNameSet = new HashSet<>();

  /**
   * このモブが持っているSkillIDを取得する
   *
   * @return
   */
  public HashSet<String> getSkillIdList() {
    return skillNameSet;
  }

  public void addSkill(String skillName) {
    skillNameSet.add(skillName);
  }

  public void executeMobSkill(LivingEntity mob, LivingEntity target, MobSkillExcuteConditionType type) {
    Set<MobSkillInterface> skillList = MobSkillManager.getSkill(skillNameSet, type);
    for (MobSkillInterface skill : skillList) {
      MobSkillExecutor.executor(mob, target, skill);
    }
  }

  int money = 10;
  int exp = -1;

  public void setExp(int exp) {
    if (exp >= 0) {
      this.exp = exp;
    }
  }

  public void setMoney(int money) {
    if (money >= 0) {
      this.money = money;
    }
  }

  @Override
  public int getExp(LastDamageMethodType type) {
    if (exp >= 0) { return exp; }
    return (int) ExpTable.getMobExp(getLbnMobTag().getLevel());
  }

  @Override
  public int getDropGalions() {
    return money;
  }

  double swordRegistance = 0;
  double bowRegistance = 0;
  double magicRegistance = 0;

  public void setSwordRegistance(double swordRegistance) {
    if (swordRegistance < 0) {
      swordRegistance = 0;
    } else if (swordRegistance > 100) {
      swordRegistance = 100;
    }
    this.swordRegistance = swordRegistance;
  }

  public void setBowRegistance(double bowRegistance) {
    if (bowRegistance < 0) {
      bowRegistance = 0;
    } else if (bowRegistance > 100) {
      bowRegistance = 100;
    }
    this.bowRegistance = bowRegistance;
  }

  public void setMagicRegistance(double magicRegistance) {
    if (magicRegistance < 0) {
      magicRegistance = 0;
    } else if (magicRegistance > 100) {
      magicRegistance = 100;
    }
    this.magicRegistance = magicRegistance;
  }

  Location redstone = null;

  public void setRedstoneLocation(Location loc) {
    redstone = loc;
  }

  double attackPoint = 1;

  public void setAttackPoint(double attackPoint) {
    this.attackPoint = attackPoint;
  }

  double defencePoint = 1;

  public void setDefencePoint(double defencePoint) {
    this.defencePoint = defencePoint;
  }

}
