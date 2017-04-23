package lbn.dungeon.contents.slotStone.level4;

import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.KillSlot;

import org.bukkit.ChatColor;

public class MagicStoneSmash extends KillSlot{

	@Override
	public String getSlotName() {
		return "Smash";
	}

	@Override
	public String getSlotDetail() {
		return "一定確率でボス以外の敵を即死させます。";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public ChatColor getNameColor() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public SlotLevel getLevel() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void onKill(PlayerKillEntityEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
