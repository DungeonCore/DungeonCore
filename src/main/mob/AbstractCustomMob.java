package main.mob;

import main.mob.customEntity.ICustomEntity;
import main.mob.customEntity1_7.CustomEntityUtil;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public abstract class AbstractCustomMob<T extends ICustomEntity<?>, K extends LivingEntity> extends AbstractMob<K>{

	@Override
	@SuppressWarnings("unchecked")
	protected K spawnPrivate(Location loc) {
		T innerEntity = getInnerEntity(loc.getWorld());
		innerEntity.setFlyMob(isFly());
		innerEntity.setIgnoreWater(isIgnoreWater());
		innerEntity.setNoKnockBackResistnce(getNoKnockback());
		K spawn = (K) innerEntity.spawn(loc);
		return spawn;
	}

	abstract protected T getInnerEntity(World w);

	@SuppressWarnings("unchecked")
	public void setNoKnockbackResistance(double val, K mob) {
		ICustomEntity<T> entity = (ICustomEntity<T>) CustomEntityUtil.getEntity(mob);
		entity.setNoKnockBackResistnce(val);
	}

	@SuppressWarnings("unchecked")
	public void setFly(boolean isFly, K mob) {
		ICustomEntity<T> entity = (ICustomEntity<T>) CustomEntityUtil.getEntity(mob);
		entity.setFlyMob(isFly);
	}


	public boolean isIgnoreWater() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public void setIgnoreWater(boolean isIgnoreWater, K mob) {
		ICustomEntity<T> entity = (ICustomEntity<T>) CustomEntityUtil.getEntity(mob);
		entity.setFlyMob(isIgnoreWater);
	}

	public boolean isFly() {
		return false;
	}

	public double getNoKnockback() {
		return 0;
	}

	@Override
	public void onOtherDamage(EntityDamageEvent e) {
		//水の中では落下ダメージを受けない
		if (isIgnoreWater()) {
			if (e.getCause() == DamageCause.DROWNING || e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}

	}
}
