package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset4;

import java.util.HashMap;
import java.util.Objects;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.particle.Particles;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import net.l_bulb.dungeoncore.util.MinecraftUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class LockOn extends WeaponSkillWithProjectile {

  static HashMap<Entity, Player> targetMap = new HashMap<>();

  @Override
  public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {}

  ParticleData particle = new ParticleData(ParticleType.portal, 100).setDispersion(1, 1, 1);

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    // パーティクルを発生させる
    TheLowExecutor.executeTimer(2,
        // 一定時間が過ぎる または モンスターが死んだら終了
        r -> (r.getAgeTick() > getData(1) * 20.0) || target.isDead(),
        (r) -> particle.runParticle((Player) owner, target.getLocation()));

    // ターゲットに追加する
    targetMap.put(target, (Player) owner);

    // ターゲットから削除する
    TheLowExecutor.executeLater((long) (getData(1) * 20.0), () -> targetMap.remove(target));
  }

  /**
   * 指定したPlayerが指定したmobをLockOnスキルでターゲティングしているならTRUE
   *
   * @param p
   * @param e
   * @return
   */
  public static boolean isTargeted(Player p, Entity e) {
    return Objects.equals(targetMap.get(p), p);
  }

  @Override
  public double fixedDamage(double damage, Entity target) {
    return damage * getData(0);
  }

  @Override
  public String getId() {
    return "wskill15";
  }

  @Override
  protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
    MinecraftUtil.playSoundForAll(p.getLocation(), Sound.SHOOT_ARROW, 1, 1);

    Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(3));
    new BukkitRunnable() {
      @Override
      public void run() {
        if (launchProjectile.isDead() || launchProjectile.isOnGround() || !launchProjectile.isValid()) {
          cancel();
          return;
        }

        Particles.runParticle(launchProjectile.getLocation(), ParticleType.portal, 10);
      }
    }.runTaskTimer(Main.plugin, 0, 2);
    return launchProjectile;
  }

}
