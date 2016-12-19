package main.dungeon.contents.mob.combination;

import main.dungeon.contents.mob.slime.HealCube;
import main.dungeon.contents.mob.zombie.NormalZombie;
import main.mob.AbstractMob;
import main.mob.mob.abstractmob.AbstractCombinationMob;
import main.util.LivingEntityUtil;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public class HealZombieCube extends AbstractCombinationMob<Slime>{

	@Override
	protected AbstractMob<?> getBaseMob() {
		return new HealCube();
	}

	@Override
	public AbstractMob<?>[] getCombinationMobListForSpawn() {
		return new AbstractMob<?>[]{getBaseMob(), new NormalZombie()};
	}

	@Override
	public String getName() {
		return "Heal Zombie Cube";
	}

	@Override
	protected void onSpawn(LivingEntity le) {
		Slime e = (Slime) le;
		e.setSize(2);
		NormalZombie normalZombie = new NormalZombie();
		Zombie spawn = normalZombie.spawn(e.getLocation());
		LivingEntityUtil.removeEquipment(spawn);

		e.setPassenger(spawn);
	}


}
