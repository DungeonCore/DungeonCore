package net.l_bulb.dungeoncore.dungeon.contents.mob.zombie;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.mob.customMob.abstractmob.AbstractZombie;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NormalZombie extends AbstractZombie {

  @Override
  public String getName() {
    return "Zombie";
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    LivingEntityUtil.removeEquipment(e.getEntity());
  }

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {}

}
