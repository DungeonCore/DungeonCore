package lbn.mob.customEntity;

import org.bukkit.Location;

public interface ICustomEntity<T> {
	public T spawn(Location loc);

	public void setNoKnockBackResistnce(double val);

	public double getNoKnockBackResistnce();

	public void setFlyMob(boolean isFly);

	public boolean isFlyMob();

	public boolean isIgnoreWater();

	public void setIgnoreWater(boolean isIgnoreWater);
}
