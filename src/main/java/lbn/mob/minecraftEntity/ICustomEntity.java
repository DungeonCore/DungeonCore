package lbn.mob.minecraftEntity;

import lbn.mob.customMob.LbnMobTag;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface ICustomEntity<T extends LivingEntity> {
  public T spawn(Location loc);

  public LbnMobTag getMobTag();
}
