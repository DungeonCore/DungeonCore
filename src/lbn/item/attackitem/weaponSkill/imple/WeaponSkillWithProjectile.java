package lbn.item.attackitem.weaponSkill.imple;

import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.player.ItemType;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public abstract class WeaponSkillWithProjectile extends WeaponSkillForOneType implements ProjectileInterface{
	public WeaponSkillWithProjectile(ItemType type) {
		super(type);
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		//Projectileを発射し、発射を記録する
		Projectile spawnedProjectile = getSpawnedProjectile(p, item, customItem);
		ProjectileManager.launchProjectile(spawnedProjectile, this, item);
		return true;
	}

	abstract protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem);

	@Override
	public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {
	}
}
