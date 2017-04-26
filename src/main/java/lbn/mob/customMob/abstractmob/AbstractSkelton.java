package lbn.mob.customMob.abstractmob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import lbn.mob.AbstractCustomMob;
import lbn.mob.minecraftEntity.CustomEntityUtil;
import lbn.mob.minecraftEntity.CustomSkeleton;

public abstract class AbstractSkelton extends AbstractCustomMob<CustomSkeleton, Skeleton> {
  /**
   * アンデット属性にするかしないかを選択
   * 
   * @param isNoUndead 初期値はFALSE
   */
  public void setNoUndead(boolean isNoUndead, Skeleton mob) {
    CustomSkeleton entity = (CustomSkeleton) CustomEntityUtil.getEntity(mob);
    entity.setUndead(!isNoUndead);
  }

  /**
   * アンデット属性でないならTRUE。通常はFALSE
   * 
   * @return
   */
  public boolean isNoUndead() {
    return false;
  }

  @Override
  protected CustomSkeleton getInnerEntity(World w) {
    return new CustomSkeleton(w, getLbnMobTag());
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.SKELETON;
  }

  @Override
  protected Skeleton spawnPrivate(Location loc) {
    Skeleton mob = super.spawnPrivate(loc);
    CustomSkeleton entity = (CustomSkeleton) CustomEntityUtil.getEntity(mob);
    entity.setNonDayFire(true);
    entity.setUndead(!isNoUndead());
    return mob;
  }

  public abstract void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e);
}
