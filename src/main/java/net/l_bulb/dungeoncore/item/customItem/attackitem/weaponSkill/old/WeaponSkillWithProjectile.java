package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;

public abstract class WeaponSkillWithProjectile extends WeaponSkillForOneType implements ProjectileInterface {

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // Projectileを発射し、発射を記録する
    Projectile spawnedProjectile = getSpawnedProjectile(p, item, customItem);
    ProjectileManager.onLaunchProjectile(spawnedProjectile, this, item);
    return true;
  }

  abstract protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem);

  @Override
  public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {}
}
