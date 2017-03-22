package lbn.item.customItem.itemAbstract;

import lbn.item.customItem.SpreadSheetItem.SpreadSheetAttackItem;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import lbn.item.itemInterface.BowItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.player.ItemType;
import lbn.util.ItemStackUtil;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BowItem extends SpreadSheetAttackItem implements BowItemable, LeftClickItemable{

	public BowItem(SpreadSheetWeaponData data) {
		super(data);
	}

	@Override
	public void onProjectileDamage(EntityDamageByEntityEvent e,
			ItemStack item, LivingEntity owner, LivingEntity target) {
	}

	@Override
	public void excuteOnShootBow(EntityShootBowEvent e) {
		LivingEntity entity = e.getEntity();
		//レベルを足りなければ弓を打たせない
		if (entity.getType() == EntityType.PLAYER) {
			if (!isAvilable((Player) entity)) {
				sendNotAvailableMessage((Player) entity);
				e.setCancelled(true);
				return;
			}
		}
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {
		//レベルなどを確認する
		Player player = e.getPlayer();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}
		if (!player.isSneaking()) {
			//スキルを発動
			WeaponSkillExecutor.executeWeaponSkillOnClick(e, this);
		}
	}

	@Override
	public ItemType getAttackType() {
		return ItemType.BOW;
	}

	@Override
	public double getMaterialDamage() {
		return ItemStackUtil.getVanillaDamage(getMaterial());
	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {

	}

}
