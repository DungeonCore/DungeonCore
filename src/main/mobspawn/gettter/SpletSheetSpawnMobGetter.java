package main.mobspawn.gettter;

import java.util.ArrayList;
import java.util.List;

import main.mob.AbstractMob;
import main.mob.MobHolder;
import main.mobspawn.SpawnLevel;
import main.mobspawn.point.MobSpawnerPoint;
import main.mobspawn.point.SpletSheetMobSpawnerPoint;

import org.bukkit.Location;

public class SpletSheetSpawnMobGetter extends AbstractSpawnMobGetter{

	public SpletSheetSpawnMobGetter(int id) {
		this.id = id;
	}

	public ArrayList<AbstractMob<?>> moblist = new ArrayList<AbstractMob<?>>();

	protected int id;

	@Override
	public String getName() {
		return Integer.toString(id);
	}

	/**
	 * 追加に成功したらTRUE
	 * @param name
	 * @return
	 */
	public boolean addMob(String name) {
		AbstractMob<?> mobWithNormal = MobHolder.getMobWithNormal(name);
		if (mobWithNormal != null) {
			moblist.add(mobWithNormal);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<AbstractMob<?>> getAllMobList() {
		return moblist;
	}

	@Override
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount,
			SpawnLevel level) {
		return new SpletSheetMobSpawnerPoint(id, loc, this, maxMobCount, level);
	}

}
