package lbn.dungeon.contents;

import lbn.dungeon.contents.spawnmob.CaveSpawnMobGetter;
import lbn.dungeon.contents.spawnmob.SkeltonMobGetter;
import lbn.dungeon.contents.spawnmob.ZombieMobGetter;
import lbn.mobspawn.gettter.SpawnMobGetterManager;

public class SpawnMobGetterRegister {
	public static void registMobGetter() {
		SpawnMobGetterManager.registSpawnMobGetter(new CaveSpawnMobGetter());
		SpawnMobGetterManager.registSpawnMobGetter(new SkeltonMobGetter());
		SpawnMobGetterManager.registSpawnMobGetter(new ZombieMobGetter());
	}
}
