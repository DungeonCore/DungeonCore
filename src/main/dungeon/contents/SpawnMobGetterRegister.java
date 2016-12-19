package main.dungeon.contents;

import main.dungeon.contents.spawnmob.CaveSpawnMobGetter;
import main.dungeon.contents.spawnmob.SkeltonMobGetter;
import main.dungeon.contents.spawnmob.ZombieMobGetter;
import main.mobspawn.gettter.SpawnMobGetterManager;

public class SpawnMobGetterRegister {
	public static void registMobGetter() {
		SpawnMobGetterManager.registSpawnMobGetter(new CaveSpawnMobGetter());
		SpawnMobGetterManager.registSpawnMobGetter(new SkeltonMobGetter());
		SpawnMobGetterManager.registSpawnMobGetter(new ZombieMobGetter());
	}
}
