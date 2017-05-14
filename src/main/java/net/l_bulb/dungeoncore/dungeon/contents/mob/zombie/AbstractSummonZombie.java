package net.l_bulb.dungeoncore.dungeon.contents.mob.zombie;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;
import net.l_bulb.dungeoncore.mob.customMob.SummonMobable;
import net.l_bulb.dungeoncore.mob.customMob.abstractmob.AbstractZombie;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public abstract class AbstractSummonZombie extends AbstractZombie implements SummonMobable {

  @Override
  public String getName() {
    return "SUMMON ZOMBIE";
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    e.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));

    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);
    new ParticleData(ParticleType.portal, 100).setDispersion(0.5, 0.5, 0.5).run(e.getEntity().getLocation().add(0, 1, 0));

    ((Zombie) e.getEntity()).setBaby(false);
    LivingEntityUtil.removeEquipment(e.getEntity());
  }

  @Override
  public double getNearingSpeed() {
    return 1.5;
  }

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
    if (target.getType() == EntityType.PLAYER) {
      e.setCancelled(true);
    } else if (MobHolder.getMob(target) instanceof SummonMobable) {
      e.setCancelled(true);
    }
  }

  @Override
  public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {}

  @Override
  public void onOtherDamage(EntityDamageEvent e) {}

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {}

  @Override
  public int getDeadlineTick() {
    return 15 * 20;
  }

  @Override
  public int getDropGalions() {
    return 0;
  }

  @Override
  public LbnMobTag getLbnMobTag() {
    LbnMobTag lbnMobTag = super.getLbnMobTag();
    lbnMobTag.setSummonMob(true);
    return lbnMobTag;
  }
}
