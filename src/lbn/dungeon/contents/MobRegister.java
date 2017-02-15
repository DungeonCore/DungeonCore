package lbn.dungeon.contents;

import lbn.dungeon.contents.mob.skelton.Arthur;
import lbn.dungeon.contents.mob.skelton.SummonSkelton;
import lbn.dungeon.contents.mob.zombie.AbstractSummonZombie;
import lbn.dungeon.contents.mob.zombie.BabyArthur;
import lbn.dungeon.contents.mob.zombie.NormalSummonZombie;
import lbn.mob.MobHolder;


public class MobRegister {
	public static void registMob() {
		MobHolder.registMob(new AbstractSummonZombie());
		MobHolder.registMob(new NormalSummonZombie(0, 0));
		MobHolder.registMob(new SummonSkelton());
		MobHolder.registMob(new Arthur());
		MobHolder.registMob(new BabyArthur());
	}
}
