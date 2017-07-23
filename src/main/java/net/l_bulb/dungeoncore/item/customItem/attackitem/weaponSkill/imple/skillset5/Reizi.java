package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset5;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class Reizi extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {}

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (getData(2) * 20.0), getDataAsInt(1) - 1), true);
  }

  @Override
  public double fixedDamage(double damage, Entity target) {
    return damage * getData(0);
  }

  @Override
  public String getId() {
    return "wskill19";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    Arrow projectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().normalize().multiply(3));

    TheLowExecutor.executeTimer(2,
        l -> projectile.isOnGround() || !projectile.isValid(),
        // パーティクルを発生させる
        r -> Particles.runParticle(projectile.getLocation(), ParticleType.lava, 10));
    return projectile;
  }

}
