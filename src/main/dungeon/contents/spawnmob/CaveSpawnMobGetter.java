package main.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import main.dungeon.contents.mob.skelton.CaveSkeleton;
import main.dungeon.contents.mob.zombie.CaveZombie;
import main.mob.AbstractMob;
import main.mobspawn.gettter.AbstractSpawnMobGetter;

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
