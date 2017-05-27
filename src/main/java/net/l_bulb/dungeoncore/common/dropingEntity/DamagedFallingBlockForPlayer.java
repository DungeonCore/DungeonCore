package net.l_bulb.dungeoncore.common.dropingEntity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public abstract class DamagedFallingBlockForPlayer extends AbstractDamageFallingblock {

  protected ItemStack item;
  protected Player p;
  protected double damage;
  private AbstractAttackItem customItem;
  private boolean isNormalAttack;

  public DamagedFallingBlockForPlayer(Player p, Material m, ItemStack item, double damage, byte data, AbstractAttackItem customItem,
      boolean isNormalAttack) {
    this(p.getLocation().getDirection(), p.getLocation().add(0, 1.2, 0), m, item, p, damage, data, customItem, isNormalAttack);
  }

  public DamagedFallingBlockForPlayer(Vector direction, Location loc, Material m, ItemStack item, Player p, double damage, byte data,
      AbstractAttackItem customItem, boolean isNormalAttack) {
    super(direction, loc, m, data);
    this.item = item;
    this.p = p;
    this.damage = damage;
    this.customItem = customItem;
    this.isNormalAttack = isNormalAttack;
  }

  @Override
  public synchronized BukkitTask runTaskTimer() throws IllegalArgumentException, IllegalStateException {
    startEntityRutine(p);
    return super.runTaskTimer();
  }

  @Override
  protected boolean damageLivingentity(Entity entity) {
    if (LivingEntityUtil.isEnemy(entity) && entity.getType().isAlive()) {
      // すでに死んでいたら何もしない
      if (entity.isDead()) { return false; }

      // LastDamageを登録
      LastDamageManager.onDamage(((LivingEntity) entity), p, getAttackType());

      // eventを実行
      PlayerCombatEntityEvent callEvent = new PlayerCombatEntityEvent(p, damage, customItem, item, isNormalAttack, (LivingEntity) entity).callEvent();
      // ダメージを与える
      ((LivingEntity) entity).damage(callEvent.getDamage(), p);

      // もしEntityが死んでいたら今の攻撃で死んだと判断しeventを実行
      if (entity.isDead()) {
        PlayerKillEntityEvent playerKillEntityEvent = new PlayerKillEntityEvent(p, (LivingEntity) entity, item);
        playerKillEntityEvent.callEvent();
      }
      return true;
    }
    return false;
  }

  protected LastDamageMethodType getAttackType() {
    return LastDamageMethodType.MAGIC;
  }

  double[] range = new double[] { 1, 1, 1 };

  @Override
  protected double[] getDamageRange() {
    return range;
  }

  @Override
  abstract public void tickRutine(int count);

  @Override
  abstract public void removedRutine(Entity spawnEntity);

  @Override
  abstract public void onHitDamagedEntity(Entity target);

  abstract public void startEntityRutine(Player p);
}
