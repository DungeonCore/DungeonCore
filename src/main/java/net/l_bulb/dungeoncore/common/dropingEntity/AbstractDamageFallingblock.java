package net.l_bulb.dungeoncore.common.dropingEntity;

import java.util.List;

import net.l_bulb.dungeoncore.util.LbnRunnable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public abstract class AbstractDamageFallingblock extends LbnRunnable {
  protected Entity spawnEntity;

  Location end;

  @SuppressWarnings("deprecation")
  public AbstractDamageFallingblock(Vector direction, Location start, Material m, byte data) {
    spawnEntity = start.getWorld().spawnFallingBlock(start, m, data);
    spawnEntity.setVelocity(direction.normalize().multiply(2));
    ((FallingBlock) spawnEntity).setDropItem(false);
  }

  public AbstractDamageFallingblock(Entity spawnedEntity) {
    spawnEntity = spawnedEntity;
  }

  public synchronized BukkitTask runTaskTimer() throws IllegalArgumentException, IllegalStateException {
    return super.runTaskTimer(1);
  }

  @Override
  public void run2() {
    double[] damageRange = getDamageRange();
    List<Entity> nearbyEntities = spawnEntity.getNearbyEntities(damageRange[0], damageRange[1], damageRange[2]);

    if (getRunCount() % 2 == 0) {
      boolean isAttack = false;

      for (Entity entity : nearbyEntities) {
        boolean damaged = damageLivingentity(entity);
        isAttack = isAttack || damaged;
        if (damaged) {
          // ダメージ時の特殊処理
          onHitDamagedEntity(entity);
        }
      }

      // 対象のEntityにぶつかったなら消す
      if (isAttack) {
        removeEntity(spawnEntity);
      }
    }
    tickRutine(getRunCount());

    // entityがなくなったら削除
    if (!spawnEntity.isValid()) {
      removeEntity(spawnEntity);
    }

    // 地面に接触していないか調べる
    if (spawnEntity.isOnGround()) {
      removeEntity(spawnEntity);
    }
    // 10秒以上飛んでいたら削除
    if (isElapsedTick(20 * 10)) {
      removeEntity(spawnEntity);
    }
  }

  abstract protected boolean damageLivingentity(Entity entity);

  double[] range = new double[] { 1, 1, 1 };

  protected double[] getDamageRange() {
    return range;
  }

  abstract public void tickRutine(int count);

  abstract public void removedRutine(Entity spawnEntity);

  abstract public void onHitDamagedEntity(Entity target);

  protected void removeEntity(Entity spawnEntity) {
    spawnEntity.remove();
    removedRutine(spawnEntity);
    cancel();
  }

  @Override
  protected void runIfServerEnd() {
    spawnEntity.remove();
  }

}
