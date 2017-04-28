package net.l_bulb.dungeoncore.mobspawn.gettter;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;

public abstract class AbstractSpawnMobGetter implements SpawnMobGetterInterface {
  @Override
  public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
    return new MobSpawnerPoint(loc, this, maxMobCount, level);
  }
}
