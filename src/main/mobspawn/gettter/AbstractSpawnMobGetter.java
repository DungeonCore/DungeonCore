package main.mobspawn.gettter;

import main.mobspawn.SpawnLevel;
import main.mobspawn.point.MobSpawnerPoint;

import org.bukkit.Location;

public abstract class AbstractSpawnMobGetter implements SpawnMobGetterInterface{
	@Override
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
		return new MobSpawnerPoint(loc, this, maxMobCount, level);
	}
}
