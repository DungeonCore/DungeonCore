package lbn.dungeon.contents.slotStone.level3;

import lbn.common.cooltime.Cooltimable;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.UnUseSlot;
import lbn.item.strength.old.StrengthOperator;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class HealSlotStone extends UnUseSlot implements Cooltimable{

	@Override
	public String getSlotName() {
		return "リジェネレーション Level1";
	}

	@Override
	public String getSlotDetail() {
		return "シフト右クリックで周囲に再生効果(中)を付与";
	}

	@Override
	public String getId() {
		return "slot_src_heal";
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
	public int getCooltimeTick(ItemStack item) {
		return 20 * 40 - StrengthOperator.getLevel(item) * 18;
	}

}
