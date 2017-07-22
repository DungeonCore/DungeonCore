package net.l_bulb.dungeoncore.common.projectile;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 矢を打って、ダメージを与えた時の処理を行うためのInterface
 *
 */
public interface ProjectileInterface {
  /**
   * Projectileを発射した時の処理
   *
   * @param e
   * @param item
   */
  public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item);

  /**
   * Projectileが当たったときの処理
   *
   * @param event
   * @param item
   * @param source
   */
  public void onProjectileHit(ProjectileHitEvent event, ItemStack item);

  /**
   * ProjectileがEntityにダメージを与えたときの処理
   *
   * @param e
   * @param item
   * @param owner
   * @param target
   */
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target);

  /**
   * Projectileを一意に認識するためのIDを取得する
   *
   * @return
   */
  public String getId();

  /**
   * Projectileによって与えるダメージを変更する
   *
   * @param damage
   * @param target ダメージを受けたEntity
   * @return
   */
  default public double fixedDamage(double damage, Entity target) {
    return damage;
  }

  /**
   * 通常攻撃ならTRUE
   *
   * @return
   */
  default public boolean isNormalAttack() {
    return true;
  }
}
