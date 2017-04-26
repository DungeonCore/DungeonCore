package lbn.mobspawn.gettter;

import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.MobSpawnerPoint;

import org.bukkit.Location;

public abstract class AbstractSpawnMobGetter implements SpawnMobGetterInterface{
	@Override
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
		return new MobSpawnerPoint(loc, this, maxMobCount, level);
	}
}
