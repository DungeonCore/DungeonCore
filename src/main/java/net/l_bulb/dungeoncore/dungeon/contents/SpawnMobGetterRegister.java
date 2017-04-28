package net.l_bulb.dungeoncore.dungeon.contents;

import net.l_bulb.dungeoncore.dungeon.contents.spawnmob.SkeltonMobGetter;
import net.l_bulb.dungeoncore.dungeon.contents.spawnmob.ZombieMobGetter;
import net.l_bulb.dungeoncore.mobspawn.gettter.SpawnMobGetterManager;

public class SpawnMobGetterRegister {
  public static void registMobGetter() {
    SpawnMobGetterManager.registSpawnMobGetter(new SkeltonMobGetter());
    SpawnMobGetterManager.registSpawnMobGetter(new ZombieMobGetter());
  }
}
