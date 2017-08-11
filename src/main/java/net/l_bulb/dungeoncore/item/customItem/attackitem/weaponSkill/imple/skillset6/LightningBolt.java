package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset6;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class LightningBolt extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    e.setDamage(getNBTTagAccessor(item).getDamage() * getData(0));
    Stun.addStun(target, (int) (getData(1) * 20));
  }

  @Override
  public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {
    Projectile entity = event.getEntity();
    LivingEntityUtil.strikeLightningEffect(entity.getLocation());
  }

  @Override
  public String getId() {
    return "wskill23";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    Vector direction = p.getLocation().getDirection().normalize();
    // double y = direction.getY();
    Fireball fireball = p.launchProjectile(Fireball.class, direction.multiply(2));
    return fireball;
  }
}
