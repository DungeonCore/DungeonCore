package main.dungeon.contents.mob.villager;

import main.dungeon.contents.mob.villager.other.BlackSmith2;
import main.dungeon.contents.mob.villager.other.Blacksmith;
import main.mob.MobHolder;

public class VillagerRegister {
	public static void regist() {
		MobHolder.registMob(new Blacksmith());
		MobHolder.registMob(new BlackSmith2());
	}
}
