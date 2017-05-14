package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;

public interface ICustomEntity<T extends LivingEntity> {
  public T spawn(Location loc);

  public LbnMobTag getMobTag();
}
