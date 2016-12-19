package lbn.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import lbn.dungeon.contents.mob.skelton.CaveSkeleton;
import lbn.dungeon.contents.mob.zombie.CaveZombie;
import lbn.mob.AbstractMob;
import lbn.mobspawn.gettter.AbstractSpawnMobGetter;

public class CaveSpawnMobGetter extends AbstractSpawnMobGetter{

	@Override
	public String getName() {
		return "CAVE1";
	}

	@Override
	public List<AbstractMob<?>> getAllMobList() {
		ArrayList<AbstractMob<?>> arrayList = new ArrayList<AbstractMob<?>>();
		arrayList.add(new CaveSkeleton());
		arrayList.add(new CaveZombie());
		arrayList.add(new CaveZombie());
		return arrayList;
	}
}
