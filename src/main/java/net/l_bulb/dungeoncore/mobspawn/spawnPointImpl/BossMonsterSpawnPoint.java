package net.l_bulb.dungeoncore.mobspawn.spawnPointImpl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.customMob.BossMobable;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;
import net.l_bulb.dungeoncore.util.DungeonLogger;

public class BossMonsterSpawnPoint extends AbstractSpawnPoint {

  private BossMobable boss;

  public BossMonsterSpawnPoint(SpawnPointSpreadSheetData data, AbstractMob<?> mob) {
    super(data);
    if (mob.isBoss()) {
      boss = (BossMobable) mob;
    }
    this.mob = mob;
  }

  protected AbstractMob<?> mob;

  @Override
  public Entity spawn() {
    return mob.spawn(getLocation());
  }

  @Override
  public boolean equalsEntity(Entity e) {
    return mob.isThisMob(e);
  }

  @Override
  public String getSpawnTargetName() {
    return mob.getName();
  }

  boolean nextSpawnFlag = true;

  @Override
  public boolean canSpawn() {
    if (boss == null) {
      getSpawnResult().setReslt(0, "ボスモンスターでないためスポーンできません");
    }

    if (nextSpawnFlag = true) {
      nextSpawnFlag = false;
      return true;
    }

    if (!super.canSpawn()) { return false; }
    LivingEntity entity = boss.getEntity();
    // Entityが生きていなければ召喚する
    if (entity != null && entity.isValid()) {
      getSpawnResult().setReslt(0, "すでに召喚されています。(0)");
      return false;
    }
    // 近くにBOSSモンスターがいるときは召喚しない
    Entity nearBoss = existMobInNear();
    if (nearBoss != null) {
      if (boss.getEntity() == null) {
        boss.setEntity((LivingEntity) nearBoss);
        DungeonLogger.error(boss.getName() + "を再セットしました。");
      }
      getSpawnResult().setReslt(0, "すでに召喚されています。(1)");
    } else {
      // 近くにBOSSモンスターがいなければ召喚できる
      nextSpawnFlag = true;
      getSpawnResult().setReslt(0, "次のタームに召喚する予定です。");
    }

    return false;
  }

  /**
   * 周囲のBOSSモンスターを取得する
   *
   * @return
   */
  private Entity existMobInNear() {
    ExperienceOrb spawn = getLocation().getWorld().spawn(getLocation(), ExperienceOrb.class);
    for (Entity livingEntity : spawn.getNearbyEntities(60, 20, 60)) {
      if (!boss.getEntityType().equals(livingEntity.getType())) {
        continue;
      }

      if (!livingEntity.isValid()) {
        continue;
      }

      if (((LivingEntity) livingEntity).getCustomName() != null
          && ((LivingEntity) livingEntity).getCustomName().contains(boss.getName())) {
        spawn.remove();
        return livingEntity;
      }
    }
    spawn.remove();
    return null;
  }
}
