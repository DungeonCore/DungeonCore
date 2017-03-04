package lbn.item.itemAbstract;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.attackitem.SpreadSheetAttackItem;
import lbn.item.attackitem.SpreadSheetWeaponData;
import lbn.item.attackitem.weaponSkill.WeaponSkillExecutor;
import lbn.item.itemInterface.BowItemable;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.strength.StrengthOperator;
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

		if (owner.getType() == EntityType.PLAYER) {
			//eventを呼ぶ
			PlayerCombatEntityEvent playerCombatEntityEvent = new PlayerCombatEntityEvent((Player)owner, target, item,
					e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getMaterialDamage());
			playerCombatEntityEvent.callEvent();
			//eventからDamageを取得
			e.setDamage(playerCombatEntityEvent.getDamage());
		} else {
			//通常通りの計算を行う
			e.setDamage(e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getMaterialDamage());
		}
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
	protected double getMaterialDamage() {
		return ItemStackUtil.getVanillaDamage(getMaterial());
	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {

	}

}
