package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset4;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.util.MinecraftUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class EnkyoriSnipe extends WeaponSkillWithProjectile {

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    double damage = getNBTTagAccessor(item).getDamage();
    if (Stun.isStun(target)) {
      damage *= getData(1);
    } else {
      damage *= getData(0);
    }
    e.setDamage(damage);
  }

  @Override
  public String getId() {
    return "wskill16";
  }

  final static Vector ZERO_VEC = new Vector(0, 0, 0);

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    MinecraftUtil.playSoundForAll(p.getLocation(), Sound.SHOOT_ARROW, 1, 1);

    // 矢が飛ぶ方向
    Vector arrowDirection = p.getLocation().getDirection().normalize().multiply(3);

    Arrow projectile = p.launchProjectile(Arrow.class, arrowDirection);

    TheLowExecutor.executeTimer(2,
        l -> projectile.isOnGround() || !projectile.isValid() || projectile.getVelocity().equals(ZERO_VEC) || l.getAgeTick() > 30 * 20,
        r -> {
          // パーティクルを発生させる
          Particles.runParticle(projectile.getLocation(), ParticleType.happyVillager, 10);
          // 方向を固定する
          projectile.setVelocity(arrowDirection);
        });

    return projectile;
  }
}
