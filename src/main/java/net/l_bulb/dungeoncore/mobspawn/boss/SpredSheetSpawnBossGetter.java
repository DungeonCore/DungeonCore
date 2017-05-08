package net.l_bulb.dungeoncore.mobspawn.boss;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.customMob.BossMobable;
import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpletSheetSpawnMobGetter;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;

import org.bukkit.Location;

public class SpredSheetSpawnBossGetter extends SpletSheetSpawnMobGetter {

  public SpredSheetSpawnBossGetter(int id) {
    super(id);
  }

  BossMobable boss;

  public BossMobable getBossMob() {
    if (boss != null) { return boss; }
    for (AbstractMob<?> abstractMob : moblist) {
      if (abstractMob instanceof BossMobable) {
        boss = (BossMobable) abstractMob;
        return boss;
      }
    }
    return null;
  }

  @Override
  public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount,
      SpawnLevel level) {
    return new BossSpawnPoint(id, loc, this);
  }

}
