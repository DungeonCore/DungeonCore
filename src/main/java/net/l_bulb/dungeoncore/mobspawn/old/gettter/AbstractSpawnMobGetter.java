package net.l_bulb.dungeoncore.mobspawn.old.gettter;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.old.point.MobSpawnerPoint;

public abstract class AbstractSpawnMobGetter implements SpawnMobGetterInterface {
  @Override
  public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
    return new MobSpawnerPoint(loc, this, maxMobCount, level);
  }
}
