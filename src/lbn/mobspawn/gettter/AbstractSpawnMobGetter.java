package lbn.mobspawn.gettter;

import org.bukkit.Location;

import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.MobSpawnerPoint;

public abstract class AbstractSpawnMobGetter implements SpawnMobGetterInterface{
	@Override
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
		return new MobSpawnerPoint(loc, this, maxMobCount, level);
	}
}
