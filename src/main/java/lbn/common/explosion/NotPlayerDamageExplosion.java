package lbn.common.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import lbn.mob.MobHolder;
import lbn.mob.customMob.SummonMobable;

public abstract class NotPlayerDamageExplosion extends AbstractNotDamageExplosion {

  public NotPlayerDamageExplosion(Location l, float f) {
    super(l, f);
  }

  @Override
  boolean isNotDamage(Entity entity) {
    if (entity.getType() == EntityType.DROPPED_ITEM) { return true; }
    if (entity instanceof LivingEntity) {
      return entity.getType() == EntityType.PLAYER || entity.getType() == EntityType.VILLAGER
          || (MobHolder.getMob((LivingEntity) entity) instanceof SummonMobable);
    } else {
      return false;
    }
  }

}
