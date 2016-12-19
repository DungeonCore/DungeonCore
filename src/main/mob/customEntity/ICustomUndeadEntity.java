package main.mob.customEntity;


public interface ICustomUndeadEntity<T> extends ICustomEntity<T>{
	public void setUndead(boolean isUndead);

	public boolean isUndead();

	public void setNonDayFire(boolean isNonDayFire);

	public boolean isNonDayFire();
}
