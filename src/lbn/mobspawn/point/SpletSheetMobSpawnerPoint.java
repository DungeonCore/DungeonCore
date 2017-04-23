package lbn.mobspawn.point;

import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.gettter.SpawnMobGetterInterface;

import org.bukkit.Location;

public class SpletSheetMobSpawnerPoint extends MobSpawnerPoint{

	protected int id;

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
