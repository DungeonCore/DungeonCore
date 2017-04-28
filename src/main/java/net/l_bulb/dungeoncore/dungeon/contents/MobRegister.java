package net.l_bulb.dungeoncore.dungeon.contents;

import net.l_bulb.dungeoncore.dungeon.contents.mob.skelton.SummonSkelton;
import net.l_bulb.dungeoncore.dungeon.contents.mob.zombie.NormalSummonZombie;
import net.l_bulb.dungeoncore.mob.MobHolder;

public class MobRegister {
  public static void registMob() {
    MobHolder.registMob(new NormalSummonZombie(0, 0));
    MobHolder.registMob(new SummonSkelton());
  }
}
