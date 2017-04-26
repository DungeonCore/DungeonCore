package lbn.dungeon.contents.mob.zombie;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.customMob.abstractmob.AbstractZombie;
import lbn.util.LivingEntityUtil;

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
