package main.dungeon.contents.slotStone.level4;

import org.bukkit.ChatColor;

import main.common.event.player.PlayerKillEntityEvent;
import main.item.slot.SlotLevel;
import main.item.slot.slot.KillSlot;

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
