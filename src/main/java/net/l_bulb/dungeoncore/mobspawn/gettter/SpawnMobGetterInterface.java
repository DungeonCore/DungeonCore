package net.l_bulb.dungeoncore.mobspawn.gettter;

import java.util.List;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.point.MobSpawnerPoint;

public interface SpawnMobGetterInterface {
  String getName();

  List<AbstractMob<?>> getAllMobList();

  MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxSpawnMob, SpawnLevel level);
}