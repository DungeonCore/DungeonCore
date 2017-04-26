package lbn.mobspawn.gettter;

import java.util.List;

import lbn.mob.AbstractMob;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.MobSpawnerPoint;

import org.bukkit.Location;

public interface SpawnMobGetterInterface {
  String getName();

  List<AbstractMob<?>> getAllMobList();

  MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxSpawnMob, SpawnLevel level);
}