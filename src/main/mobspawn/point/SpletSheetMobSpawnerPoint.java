package main.mobspawn.point;

import main.mobspawn.SpawnLevel;
import main.mobspawn.gettter.SpawnMobGetterInterface;

import org.bukkit.Location;

public class SpletSheetMobSpawnerPoint extends MobSpawnerPoint{

	int id;

	public SpletSheetMobSpawnerPoint(int id, Location loc,
			SpawnMobGetterInterface mobGetter, int maxMobCount, SpawnLevel level) {
		super(loc, mobGetter, maxMobCount, level);
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}
}
