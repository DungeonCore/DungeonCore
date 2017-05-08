package net.l_bulb.dungeoncore.dungeon.contents.mob;

import net.l_bulb.dungeoncore.dungeon.contents.mob.skelton.NormalSkelton;
import net.l_bulb.dungeoncore.dungeon.contents.mob.zombie.NormalZombie;
import net.l_bulb.dungeoncore.mob.customMob.NullMob;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class NormalMob extends NullMob {
  EntityType type;

  public NormalMob(EntityType type) {
    this.type = type;
  }

  @Override
  public EntityType getEntityType() {
    return type;
  }

  @Override
  public LivingEntity spawnPrivate(Location loc) {
    if (type.equals(EntityType.ZOMBIE)) {
      return new NormalZombie().spawn(loc);
    } else if (type.equals(EntityType.SKELETON)) { return new NormalSkelton().spawn(loc); }
    LivingEntity spawnEntity = (LivingEntity) loc.getWorld().spawnEntity(loc, getEntityType());
    LivingEntityUtil.removeEquipment(spawnEntity, false);
    return spawnEntity;
  }

  @Override
  public boolean isNullMob() {
    return true;
  }
}
