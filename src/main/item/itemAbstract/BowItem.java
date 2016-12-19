package main.item.itemAbstract;

import main.common.event.player.PlayerCombatEntityEvent;
import main.item.ItemInterface;
import main.item.attackitem.AbstractAttackItem;
import main.item.itemInterface.BowItemable;
import main.item.itemInterface.LeftClickItemable;
import main.item.strength.StrengthOperator;
import main.player.AttackType;
import main.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BowItem extends AbstractAttackItem implements  ItemInterface, BowItemable, LeftClickItemable{
	@Override
	protected Material getMaterial() {
		return Material.BOW;
	}

	@Override
	public void excuteOnProjectileDamage(EntityDamageByEntityEvent e,
			ItemStack item, LivingEntity owner, LivingEntity target) {

		if (owner.getType() == EntityType.PLAYER) {
			//eventを呼ぶ
			PlayerCombatEntityEvent playerCombatEntityEvent = new PlayerCombatEntityEvent((Player)owner, target, item,
					e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getNormalDamage());
			playerCombatEntityEvent.callEvent();
			//eventからDamageを取得
			e.setDamage(playerCombatEntityEvent.getDamage());
		} else {
			//通常通りの計算を行う
			e.setDamage(e.getDamage() + getAttackItemDamage(StrengthOperator.getLevel(item)) - getNormalDamage());
		}
	}

	abstract protected void excuteOnShootBow2(EntityShootBowEvent e);

	@Override
	public void excuteOnShootBow(EntityShootBowEvent e) {
		LivingEntity entity = e.getEntity();
		if (entity.getType() == EntityType.PLAYER) {
			if (!isAvilable((Player) entity)) {
				sendNotAvailableMessage((Player) entity);
				e.setCancelled(true);
				return;
			}
		}
		excuteOnShootBow2(e);
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			e.setCancelled(true);
			return;
		}
		excuteOnLeftClick2(e);
	}

	abstract protected void excuteOnLeftClick2(PlayerInteractEvent e);

	@Override
	public AttackType getAttackType() {
		return AttackType.BOW;
	}

	abstract public int getAvailableLevel();

	@Override
	protected double getNormalDamage() {
		return ItemStackUtil.getVanillaDamage(getMaterial());
	}
}
