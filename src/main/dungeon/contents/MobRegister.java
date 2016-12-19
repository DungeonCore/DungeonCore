package main.dungeon.contents;

import main.dungeon.contents.mob.combination.FireSkeltonCube;
import main.dungeon.contents.mob.combination.HealZombieCube;
import main.dungeon.contents.mob.combination.MiniSlimeZombie;
import main.dungeon.contents.mob.combination.SkeltonRider;
import main.dungeon.contents.mob.skelton.Arion;
import main.dungeon.contents.mob.skelton.Arthur;
import main.dungeon.contents.mob.skelton.CaveSkeleton;
import main.dungeon.contents.mob.skelton.ExplosionSkeleton;
import main.dungeon.contents.mob.skelton.KumajyagaSkelton;
import main.dungeon.contents.mob.skelton.MineralizationArcher;
import main.dungeon.contents.mob.skelton.SummonSkelton;
import main.dungeon.contents.mob.skelton.Tadmor;
import main.dungeon.contents.mob.slime.FireCube;
import main.dungeon.contents.mob.slime.HealCube;
import main.dungeon.contents.mob.villager.VillagerRegister;
import main.dungeon.contents.mob.zombie.AbstractSummonZombie;
import main.dungeon.contents.mob.zombie.BabyArthur;
import main.dungeon.contents.mob.zombie.Bondage;
import main.dungeon.contents.mob.zombie.CaveZombie;
import main.dungeon.contents.mob.zombie.Hermit;
import main.dungeon.contents.mob.zombie.Highwayman;
import main.dungeon.contents.mob.zombie.MineralizationSoldier;
import main.dungeon.contents.mob.zombie.NormalSummonZombie;
import main.dungeon.contents.mob.zombie.Saratoga_v1;
import main.dungeon.contents.mob.zombie.ShyZombie;
import main.dungeon.contents.mob.zombie.SuicideAttackZombie;
import main.dungeon.contents.mob.zombie.WaterZombie;
import main.mob.MobHolder;


public class MobRegister {
	public static void registMob() {
		MobHolder.registMob(new WaterZombie());
		MobHolder.registMob(new AbstractSummonZombie());
		MobHolder.registMob(new SummonSkelton());
//		MobHolder.registMob(new TestBossWitherSkelton());
		MobHolder.registMob(new ExplosionSkeleton());
//		MobHolder.registMob(new Spider());
		MobHolder.registMob(new Arthur());
		MobHolder.registMob(new BabyArthur());
		MobHolder.registMob(new SuicideAttackZombie());
		MobHolder.registMob(new ShyZombie());
		MobHolder.registMob(new Highwayman());
		MobHolder.registMob(new MineralizationSoldier());
		MobHolder.registMob(new MineralizationArcher());
		MobHolder.registMob(new Arion());
		MobHolder.registMob(new Tadmor());
//		MobHolder.registMob(new Witch());
		MobHolder.registMob(new CaveZombie());
		MobHolder.registMob(new CaveSkeleton());
//		MobHolder.registMob(new SkeltonRiderSkelton());
		MobHolder.registMob(new HealCube());
		MobHolder.registMob(new FireCube());
		MobHolder.registMob(new Saratoga_v1());
//		MobHolder.registMob(new Book());
//		MobHolder.registMob(new Ninja());
		MobHolder.registMob(new Bondage());
		MobHolder.registMob(new KumajyagaSkelton());

		MobHolder.registMob(new SkeltonRider());
		MobHolder.registMob(new FireSkeltonCube());
		MobHolder.registMob(new HealZombieCube());
		MobHolder.registMob(new MiniSlimeZombie());

		MobHolder.registMob(new Hermit());
		MobHolder.registMob(new NormalSummonZombie(0, 0));

		VillagerRegister.regist();
	}
}
