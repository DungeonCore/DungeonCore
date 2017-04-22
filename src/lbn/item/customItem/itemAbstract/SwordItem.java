package lbn.item.customItem.itemAbstract;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.customItem.SpreadSheetItem.SpreadSheetAttackItem;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.attackitem.weaponSkill.WeaponSkillExecutor;
import lbn.item.itemInterface.MeleeAttackItemable;
import lbn.item.system.strength.StrengthOperator;
import lbn.player.ItemType;
import lbn.util.ItemStackUtil;
import lbn.util.LivingEntityUtil;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SwordItem extends SpreadSheetAttackItem implements MeleeAttackItemable {
	public SwordItem(SpreadSheetWeaponData data) {
		super(data);
	}

	public int rank() {
		return 0;
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		super.excuteOnRightClick(e);
		// レベルなどを確認する
		Player player = e.getPlayer();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}
		if (!e.getPlayer().isSneaking()) {
			// スキルを発動
			WeaponSkillExecutor.executeWeaponSkillOnClick(e, this);
		}
	}

	@Override
	public double getMaterialDamage() {
		return ItemStackUtil.getVanillaDamage(getMaterial());
	}

	@Override
	public void excuteOnMeleeAttack(ItemStack item, LivingEntity owner, LivingEntity target,
			EntityDamageByEntityEvent e) {
		// プレイヤーでないなら関係ない
		if (owner.getType() != EntityType.PLAYER) {
			return;
		}

		owner.sendMessage(getAttackItemDamage(0) + "");

		Player player = (Player) owner;
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}

		if (LivingEntityUtil.isEnemy(target)) {
			// eventを呼ぶ
			// 相殺されるはず(e.getDamage() - getNormalDamage() )
			PlayerCombatEntityEvent playerCombatEntityEvent = new PlayerCombatEntityEvent(player, target, item,
					e.getDamage() - getMaterialDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)));
			playerCombatEntityEvent.callEvent();
			// ダメージの計算を行う
			e.setDamage(playerCombatEntityEvent.getDamage());
		} else {
			e.setDamage(e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getMaterialDamage());
		}
	}

	@Override
	public ItemType getAttackType() {
		return ItemType.SWORD;
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {
	}

}
