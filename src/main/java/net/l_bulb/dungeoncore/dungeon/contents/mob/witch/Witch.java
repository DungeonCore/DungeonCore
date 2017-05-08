package net.l_bulb.dungeoncore.dungeon.contents.mob.witch;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.mob.customMob.abstractmob.AbstractWitch;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Witch extends AbstractWitch {

  @Override
  public String getName() {
    return "WITCH";
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {}

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDamage(LivingEntity mob, Entity damager,
      EntityDamageByEntityEvent e) {

  }

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {

  }

}
