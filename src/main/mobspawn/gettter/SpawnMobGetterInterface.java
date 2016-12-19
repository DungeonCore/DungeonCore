package main.mobspawn.gettter;

import java.util.List;

import main.mob.AbstractMob;
import main.mobspawn.SpawnLevel;
import main.mobspawn.point.MobSpawnerPoint;

import org.bukkit.Location;

public interface SpawnMobGetterInterface {
	String getName();

	List<AbstractMob<?>> getAllMobList();

	MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxSpawnMob, SpawnLevel level);
}