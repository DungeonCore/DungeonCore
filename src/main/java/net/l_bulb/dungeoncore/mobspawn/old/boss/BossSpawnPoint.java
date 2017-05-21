package net.l_bulb.dungeoncore.mobspawn.old.boss;

import java.text.MessageFormat;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.customMob.BossMobable;
import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.old.point.SpletSheetMobSpawnerPoint;
import net.l_bulb.dungeoncore.util.DungeonLogger;

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
    Entity existMobInNear = existMobInNear();
    if (existMobInNear != null) {
      nextSpawn = false;
      cancelReson = MessageFormat.format("mob is alive yet error.({0},{1},{2}) mob is exist yet!!", existMobInNear.getLocation().getBlockX(),
          existMobInNear.getLocation().getBlockY(), existMobInNear.getLocation().getBlockZ());
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

  private Entity existMobInNear() {
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
        return livingEntity;
      }
    }
    spawn.remove();
    return null;
  }

  public boolean isAliveEntity(LivingEntity entity) {
    if (entity == null) {
      // DungeonLog.printDevelopln(boss.getName() + " is null. So buss will be
      // spawned!!");
      return false;
    }

    if (!entity.isValid()) {
      // DungeonLog.printDevelopln(boss.getName() + " is dead!!");
      return false;
    }

    if (((Damageable) entity).getHealth() > 0) { return true; }

    DungeonLogger.development(entity.getCustomName() + " is not alive. So buss will be spawned!!");
    return false;
  }

}
