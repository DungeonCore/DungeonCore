package lbn.mobspawn.gettter;

import java.util.ArrayList;
import java.util.List;

import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.point.MobSpawnerPoint;
import lbn.mobspawn.point.SpletSheetMobSpawnerPoint;

import org.bukkit.Location;

public class SpletSheetSpawnMobGetter extends AbstractSpawnMobGetter {

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
	 * 
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
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount, SpawnLevel level) {
		return new SpletSheetMobSpawnerPoint(id, loc, this, maxMobCount, level);
	}

}
