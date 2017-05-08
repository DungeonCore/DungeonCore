package net.l_bulb.dungeoncore.mob.minecraftEntity;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface ICustomEntity<T extends LivingEntity> {
  public T spawn(Location loc);

  public LbnMobTag getMobTag();
}
