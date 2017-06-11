package net.l_bulb.dungeoncore.mobspawn.spawnPointImpl;

import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;

public class MonsterSpawnPoint extends AbstractSpawnPoint {

  protected AbstractMob<?> mob;

  public MonsterSpawnPoint(SpawnPointSpreadSheetData data, AbstractMob<?> mob) {
    super(data);
    this.mob = mob;
  }

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

  @Override
  public boolean canSpawn() {
    if (mob.isBoss()) {
      getSpawnResult().setReslt(0, "ボスモンスターのためスポーンできません。");
      return false;
    }
    return super.canSpawn();
  }
}
