package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset6;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;

public class MagicBall extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    e.setDamage(getNBTTagAccessor(item).getDamage() * getData(0));
  }

  @Override
  public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {
    Location location = event.getEntity().getLocation();
    Particles.runParticle(location, ParticleType.explode);
  }

  @Override
  public String getId() {
    return "wskill22";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    Vector direction = p.getLocation().getDirection().normalize();
    // double y = direction.getY();
    Fireball fireball = p.launchProjectile(Fireball.class, direction.multiply(2));
    return fireball;
  }
}
