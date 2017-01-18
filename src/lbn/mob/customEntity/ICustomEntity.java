package lbn.mob.customEntity;

import org.bukkit.Location;

public interface ICustomEntity<T> {
	public T spawn(Location loc);
}
