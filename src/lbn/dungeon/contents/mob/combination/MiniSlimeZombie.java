package lbn.dungeon.contents.mob.combination;

import lbn.dungeon.contents.mob.NormalMob;
import lbn.mob.AbstractMob;
import lbn.mob.mob.abstractmob.AbstractCombinationMob;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public class MiniSlimeZombie extends AbstractCombinationMob<Slime>{

	@Override
	protected void onSpawn(LivingEntity spawnMob) {
		Slime slime = (Slime) spawnMob;
		slime.setSize(2);

		Zombie z = (Zombie) zombie.spawn(slime.getLocation());
		z.setBaby(true);

		slime.setPassenger(z);
	}

	static NormalMob slime = new NormalMob(EntityType.SLIME);
	static NormalMob zombie = new NormalMob(EntityType.ZOMBIE);

	@Override
	protected AbstractMob<?> getBaseMob() {
		return slime;
	}

	@Override
	public AbstractMob<?>[] getCombinationMobListForSpawn() {
		return new AbstractMob<?>[]{getBaseMob(), zombie};
	}

	@Override
	public String getName() {
		return "mini slime zombie";
	}

}
