package lbn.mobspawn.gettter;

import java.util.List;

import org.bukkit.Location;

import lbn.mob.AbstractMob;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.MobSpawnerPoint;

public interface SpawnMobGetterInterface {
  String getName();

  List<AbstractMob<?>> getAllMobList();

  MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxSpawnMob, SpawnLevel level);
}