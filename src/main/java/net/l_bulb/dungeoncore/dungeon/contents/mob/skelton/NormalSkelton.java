package net.l_bulb.dungeoncore.dungeon.contents.mob.skelton;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.mob.customMob.abstractmob.AbstractSkelton;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NormalSkelton extends AbstractSkelton {

  @Override
  public void onProjectileHitEntity(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {

  }

  @Override
  public String getName() {
    return "Skeleton";
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    LivingEntityUtil.removeEquipment(e.getEntity(), false);
  }

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDamage(LivingEntity mob, Entity damager,
      EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {}

}
