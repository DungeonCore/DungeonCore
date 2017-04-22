package lbn.item.slot.slot;

import lbn.item.slot.SlotInterface;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.SlotType;

import org.bukkit.ChatColor;

public class EmptySlot implements SlotInterface {

	@Override
	public String getSlotName() {
		return "空のスロット";
	}

	@Override
	public String getSlotDetail() {
		return "スロットを装着できます";
	}

	@Override
	public String getId() {
		return "empty";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.WHITE;
	}

	@Override
	public SlotType getSlotType() {
		return SlotType.EMPTY;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.UNUSE;
	}

	@Override
	public boolean isSame(SlotInterface slot) {
		if (slot != null) {
			return getId().equals(slot.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SlotInterface) {
			return getId().equals(((SlotInterface) obj).getId());
		} else {
			return false;
		}
	}
}
