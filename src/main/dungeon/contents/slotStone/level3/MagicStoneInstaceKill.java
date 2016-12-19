package main.dungeon.contents.slotStone.level3;

import main.common.event.player.PlayerCombatEntityEvent;
import main.item.slot.SlotLevel;
import main.item.slot.slot.CombatSlot;

import org.bukkit.ChatColor;

public class MagicStoneInstaceKill extends CombatSlot{

	@Override
	public String getSlotName() {
		return "スレイヤー";
	}

	@Override
	public String getSlotDetail() {
		return "一定確率でボス以外の敵を即死させる";
	}

	@Override
	public String getId() {
		return "magic_stone_slyer";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.LEVEL3;
	}

	@Override
	public void onCombat(PlayerCombatEntityEvent e) {
//		LivingEntity enemy = e.getEnemy();
//		AbstractMob<?> mob = MobHolder.getMob(enemy);
	}
}
