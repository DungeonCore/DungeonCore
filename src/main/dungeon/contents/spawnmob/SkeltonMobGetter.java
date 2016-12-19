package main.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import main.dungeon.contents.mob.NormalMob;
import main.mob.AbstractMob;
import main.mobspawn.gettter.AbstractSpawnMobGetter;

import org.bukkit.entity.EntityType;

public class SkeltonMobGetter extends AbstractSpawnMobGetter{

	@Override
	public String getName() {
		return "skelton";
	}

	static ArrayList<AbstractMob<?>> mobList = new ArrayList<AbstractMob<?>>();
	static {
		mobList.add(new NormalMob(EntityType.SKELETON));
	}
	@Override
	public List<AbstractMob<?>> getAllMobList() {
		return mobList;
	}

}
