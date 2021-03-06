package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.player.ItemType;

public class IceArrow extends WeaponSkillWithProjectile {

  public IceArrow() {
    super(ItemType.BOW);
  }

  @Override
  public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

  }

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    Stun.addStun(target, (int) (20 * getData(0)));
    Particles.runParticle(target.getEyeLocation(), ParticleType.snowballpoof, 100);
  }

  @Override
  public String getId() {
    return "skill4";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(3));
    new BukkitRunnable() {
      @Override
      public void run() {
        if (launchProjectile.isDead() || launchProjectile.isOnGround() || !launchProjectile.isValid()) {
          cancel();
          return;
        }

        Particles.runParticle(launchProjectile.getLocation(), ParticleType.snowballpoof, 10);
      }
    }.runTaskTimer(Main.plugin, 0, 2);
    return launchProjectile;
  }
}
