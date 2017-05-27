package net.l_bulb.dungeoncore.common.explosion;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class MobAttackExplosion extends NotMonsterDamageExplosion {

  public MobAttackExplosion(Location l, float f, LivingEntity mob) {
    super(l, f);
    this.mob = mob;
  }

  LivingEntity mob;

  @Override
  public float getDamage(Entity craftEntity, float d10) {
    return d10;
  }
}
