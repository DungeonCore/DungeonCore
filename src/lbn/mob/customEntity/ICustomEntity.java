package lbn.mob.customEntity;

import lbn.mob.mob.LbnMobTag;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface ICustomEntity<T extends LivingEntity> {
	public T spawn(Location loc);

	public LbnMobTag getMobTag();
}
