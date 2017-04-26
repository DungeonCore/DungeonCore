package lbn.mobspawn.boss;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;

import lbn.mob.AbstractMob;
import lbn.mob.customMob.BossMobable;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.SpletSheetMobSpawnerPoint;
import lbn.util.DungeonLogger;

public class BossSpawnPoint extends SpletSheetMobSpawnerPoint {

  BossMobable boss;
  Location spawnPointLoc;

  protected BossSpawnPoint(int id, Location loc, SpredSheetSpawnBossGetter mobGetter) {
    super(id, loc, mobGetter, 1, SpawnLevel.BOSS);
    boss = mobGetter.getBossMob();
    spawnPointLoc = loc;
  }

  boolean nextSpawn = false;

  @Override
  public int spawnMob() {
    if (boss == null) {
      DungeonLogger.error("mob is invalid:" + mobGetter.getName() + "(spawnpoint:" + id + ")");
      cancelReson = "mob is not found. (id " + mobGetter.getName() + ")";
      return 0;
    }
    // //まだ生きているなら何もしない
    LivingEntity entity = boss.getEntity();
    if (isAliveEntity(entity)) {
      // if (entity != null && entity.isValid()) {
      nextSpawn = false;
      Location location = entity.getLocation();
      cancelReson = "mob is alive yet. (" + location.getBlockX() + ", " + location.getBlockY() + ", "
          + location.getBlockZ() + ") entity is valid:" + entity.isValid();
      return 0;
    }

    if (!nextSpawn) {
      // 次の捜査でスポーンさせる
      nextSpawn = true;
      cancelReson = "mob will be spawned at next term, entity is null:" + (entity == null);
      return 0;
    }

    // スポーンの周りを調べる
    if (existMobInNear()) {
      nextSpawn = false;
      cancelReson = "occure mob is alive yet error. mob is exist yet!!";
      return 0;
    }

    AbstractMob<?> mob = (AbstractMob<?>) boss;
    Entity spawn = mob.spawn(loc);
    if (spawn == null) {
      nextSpawn = false;
      cancelReson = "occure mob is alive yet error. mob is null";
      return 0;
    }

    nextSpawn = false;

    lastSpawnTime = System.currentTimeMillis();
    lastSpawnCount = 1;
    cancelReson = null;

    return 1;
  }

  private boolean existMobInNear() {
    ExperienceOrb spawn = spawnPointLoc.getWorld().spawn(spawnPointLoc, ExperienceOrb.class);
    for (Entity livingEntity : spawn.getNearbyEntities(60, 20, 60)) {
      if (!boss.getEntityType().equals(livingEntity.getType())) {
        continue;
      }

      if (!livingEntity.getType().isAlive()) {
        continue;
      }

      if (!livingEntity.isValid()) {
        continue;
      }

      if (((LivingEntity) livingEntity).getCustomName() != null
          && ((LivingEntity) livingEntity).getCustomName().contains(boss.getName())) {
        if (boss.getEntity() == null) {
          boss.setEntity((LivingEntity) livingEntity);
          DungeonLogger.error(boss.getName() + "を再セットしました。");
        } else {
          DungeonLogger.error(
              boss.getName() + "が存在してるのにも関わらず、システムはボスモンスターを再召喚しようとしました。 entity is dead:" + boss.getEntity().isDead());
        }
        spawn.remove();
        return true;
      }
    }
    spawn.remove();
    return false;
  }

  public boolean isAliveEntity(LivingEntity entity) {
    if (entity == null) {
      // DungeonLog.printDevelopln(boss.getName() + " is null. So buss will be
      // spawned!!");
      return false;
    }

    if (entity.isDead()) {
      // DungeonLog.printDevelopln(boss.getName() + " is dead!!");
      return false;
    }

    if (((Damageable) entity).getHealth() > 0) { return true; }

    DungeonLogger.development(entity.getCustomName() + " is not alive. So buss will be spawned!!");
    return false;
  }

}
