package lbn.dungeon.contents.mob.combination;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

import lbn.dungeon.contents.mob.slime.HealCube;
import lbn.dungeon.contents.mob.zombie.NormalZombie;
import lbn.mob.AbstractMob;
import lbn.mob.mob.abstractmob.AbstractCombinationMob;
import lbn.util.LivingEntityUtil;

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
