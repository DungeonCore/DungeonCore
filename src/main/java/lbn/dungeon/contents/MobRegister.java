package lbn.dungeon.contents;

import lbn.dungeon.contents.mob.skelton.SummonSkelton;
import lbn.dungeon.contents.mob.zombie.NormalSummonZombie;
import lbn.mob.MobHolder;

public class MobRegister {
  public static void registMob() {
    MobHolder.registMob(new NormalSummonZombie(0, 0));
    MobHolder.registMob(new SummonSkelton());
  }
}
