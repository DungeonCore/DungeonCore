package lbn.dungeon.contents.mob.villager;

import lbn.dungeon.contents.mob.villager.other.BlackSmith2;
import lbn.dungeon.contents.mob.villager.other.Blacksmith;
import lbn.mob.MobHolder;

public class VillagerRegister {
	public static void regist() {
		MobHolder.registMob(new Blacksmith());
		MobHolder.registMob(new BlackSmith2());
	}
}
