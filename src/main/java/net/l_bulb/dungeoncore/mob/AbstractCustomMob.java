package net.l_bulb.dungeoncore.mob;

import net.l_bulb.dungeoncore.mob.minecraftEntity.ICustomEntity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public abstract class AbstractCustomMob<T extends ICustomEntity<?>, K extends LivingEntity> extends AbstractMob<K> {

  @Override
  @SuppressWarnings("unchecked")
  protected K spawnPrivate(Location loc) {
    T innerEntity = getInnerEntity(loc.getWorld());
    K spawn = (K) innerEntity.spawn(loc);
    return spawn;
  }

  abstract protected T getInnerEntity(World w);

  public boolean isIgnoreWater() {
    return false;
  }

  public boolean isFly() {
    return false;
  }

  public double getNoKnockback() {
    return 0;
  }

  @Override
  public void onOtherDamage(EntityDamageEvent e) {
    // 水の中では落下ダメージを受けない
    if (isIgnoreWater()) {
      if (e.getCause() == DamageCause.DROWNING || e.getCause() == DamageCause.FALL) {
        e.setCancelled(true);
      }
    }

  }
}
