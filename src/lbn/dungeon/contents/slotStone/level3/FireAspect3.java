package lbn.dungeon.contents.slotStone.level3;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.CombatSlot;
import lbn.util.LivingEntityUtil;

public class FireAspect3 extends CombatSlot{

	@Override
	public String getSlotName() {
		return "レーヴァティン Level3";
	}

	@Override
	public String getSlotDetail() {
		return "攻撃を与えた敵と周りの敵に延焼効果を付与";
	}

	@Override
	public String getId() {
		return "slot_fire_3";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.RED;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEVEL3;
	}

	@Override
	public void onCombat(PlayerCombatEntityEvent e) {
		e.getEnemy().setFireTicks(20 * 6);

		for (Entity entity : e.getEnemy().getNearbyEntities(3, 3, 3)) {
			if (LivingEntityUtil.isEnemy(entity)) {
				((LivingEntity)entity).setFireTicks(20 * 4);
			}
		}
	}

}
