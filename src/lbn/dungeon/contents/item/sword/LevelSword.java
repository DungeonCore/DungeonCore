package lbn.dungeon.contents.item.sword;

import lbn.item.customItem.attackitem.old.SwordItemOld;
import lbn.item.system.strength.StrengthOperator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class LevelSword extends SwordItemOld{

	protected int getStrengthLevel(LivingEntity e) {
		return StrengthOperator.getLevel(e.getEquipment().getItemInHand());
	}

	@Override
	public void excuteOnLeftClick(PlayerInteractEvent e) {
	}

	@Override
	protected int getBaseBuyPrice() {
		return 100;
	}
}
