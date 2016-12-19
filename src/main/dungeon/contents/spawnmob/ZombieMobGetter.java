package main.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import main.dungeon.contents.mob.NormalMob;
import main.mob.AbstractMob;
import main.mobspawn.gettter.AbstractSpawnMobGetter;

import org.bukkit.entity.EntityType;

public class ZombieMobGetter  extends AbstractSpawnMobGetter{

	@Override
	public String getName() {
		return "zombie";
	}

	static ArrayList<AbstractMob<?>> mobList = new ArrayList<AbstractMob<?>>();
	static {
		mobList.add(new NormalMob(EntityType.ZOMBIE));
	}

	@Override
	public List<AbstractMob<?>> getAllMobList() {
		return mobList;
	}

}
