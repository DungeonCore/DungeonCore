package lbn.util.dropingEntity;

import lbn.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class DamageFallingblockForMonsterSkill extends AbstractDamageFallingblock{

	Entity mob;
	Vector direction;

	public DamageFallingblockForMonsterSkill(Entity mob, Location target, Material m, byte data, double speed) {
		super(target.toVector().subtract(mob.getLocation().toVector()).normalize(), mob.getLocation().add(0, 1.5, 0), m, data);
		spawnEntity.setVelocity(spawnEntity.getVelocity().multiply(speed / 2.0));
		direction = spawnEntity.getVelocity();
		this.mob = mob;
	}

	@Override
	public void run2() {
		super.run2();
		//6秒以上飛んでいたら削除
		if (isElapsedTick(20 * 6)) {
			removeEntity(spawnEntity);
		}
	}

	@Override
	protected boolean damageLivingentity(Entity entity) {
		if (LivingEntityUtil.isFriendship(entity) && entity.getType() != EntityType.VILLAGER && entity.getType().isAlive()) {
			executeDamage((LivingEntity) entity, (LivingEntity) mob);
			return true;
		}
		return false;
	}

	abstract protected void executeDamage(LivingEntity target, LivingEntity mob);

	boolean isFirst = true;
	boolean confirictWall = false;

	@Override
	public void tickRutine(int count) {
		if (isFirst) {
			isFirst = false;
			spawnEntity.setVelocity(direction.setY(direction.getY()));
			return;
		}

		//xとzが違うなら壁に衝突したと考える
		if (!confirictWall && (spawnEntity.getVelocity().getX() != direction.getX() || spawnEntity.getVelocity().getY() != direction.getY())) {
			confirictWall = true;
		}

		if (confirictWall) {
			spawnEntity.setVelocity(direction.setY(direction.getY()));
		}
	}

	@Override
	public void removedRutine(Entity spawnEntity) {
	}

	@Override
	public void damagedEntityRutine(Entity target) {
	}

}
