package lbn.item.attackitem.weaponSkill.imple.bow;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lbn.common.other.ItemStackData;
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
		Stun.addStun(target, 20 * 1);
	}

	@Override
	public String getId() {
		return "ice_arrow";
	}

	@Override
	public int getSkillLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "アイスアロー";
	}

	@Override
	public String[] getDetail() {
		return new String[]{"矢を飛ばし、当てた敵に１秒間のスタン効果を与える"};
	}

	@Override
	public int getCooltime() {
		return 60;
	}

	@Override
	public int getNeedMagicPoint() {
		return 30;
	}

	@Override
	public ItemStackData getViewItemStackData() {
		return new ItemStackData(Material.ICE);
	}

	@Override
	protected Projectile getSpawnedProjectile(Player p, ItemStack item, AbstractAttackItem customItem) {
		Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().add(new Vector(0, 0.5, 0)).multiply(2));
		return launchProjectile;
	}
}
