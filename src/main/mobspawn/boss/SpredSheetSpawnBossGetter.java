package main.mobspawn.boss;

import org.bukkit.Location;

import main.mob.AbstractMob;
import main.mob.mob.BossMobable;
import main.mobspawn.SpawnLevel;
import main.mobspawn.gettter.SpletSheetSpawnMobGetter;
import main.mobspawn.point.MobSpawnerPoint;

public class SpredSheetSpawnBossGetter extends SpletSheetSpawnMobGetter{

	public SpredSheetSpawnBossGetter(int id) {
		super(id);
	}

	BossMobable boss;

	public BossMobable getBossMob() {
		if (boss != null) {
			return boss;
		}
		for (AbstractMob<?> abstractMob : moblist) {
			if (abstractMob instanceof BossMobable) {
				boss = (BossMobable) abstractMob;
				return boss;
			}
		}
		return null;
	}

	@Override
	public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount,
			SpawnLevel level) {
		return new BossSpawnPoint(id, loc, this);
	}

}
