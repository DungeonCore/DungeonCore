package lbn.item.attackitem.weaponSkill.imple.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lbn.common.other.Stun;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillWithProjectile;
import lbn.player.ItemType;

public class IceArrow extends WeaponSkillWithProjectile{

	public IceArrow() {
		super(ItemType.BOW);
	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

	}

	@Override
	public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
		Stun.addStun(target, (int) (20 * getData(0)));
	}

	@Override
	public String getId() {
		return "skill4";
	}

	@Override
	protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
		Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(2));
		return launchProjectile;
	}
}
