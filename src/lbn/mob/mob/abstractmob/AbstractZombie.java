package lbn.mob.mob.abstractmob;

import lbn.mob.AbstractCustomMob;
import lbn.mob.customEntity1_7.CustomEntityUtil;
import lbn.mob.customEntity1_7.CustomZombie;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public abstract class AbstractZombie extends AbstractCustomMob<CustomZombie, Zombie>{
	/**
	 * アンデット属性にするかしないかを選択
	 * @param isNoUndead 初期値はFALSE
	 */
	public void setNoUndead(boolean isNoUndead, Zombie mob) {
		CustomZombie entity = (CustomZombie) CustomEntityUtil.getEntity(mob);
		entity.setUndead(!isNoUndead);
	}


	@Override
	public CustomZombie getInnerEntity(World w) {
		return new CustomZombie(w, getLbnMobTag());
	}

	public double getNearingSpeed() {
		return 1.5;
	}


	/**
	 * アンデット属性でないならTRUE。通常はFALSE
	 * @return
	 */
	public boolean isNoUndead() {
		return false;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

	@Override
	protected Zombie spawnPrivate(Location loc) {
		Zombie mob = super.spawnPrivate(loc);
		CustomZombie entity = (CustomZombie) CustomEntityUtil.getEntity(mob);
		entity.setNonDayFire(true);
		entity.setUndead(!isNoUndead());
		return mob;
	}

	public boolean isAvoidPlayer() {
		return false;
	}
}
