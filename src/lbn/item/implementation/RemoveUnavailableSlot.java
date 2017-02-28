package lbn.item.implementation;

import java.util.Arrays;
import java.util.List;

import lbn.item.AbstractItem;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.SlotType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class RemoveUnavailableSlot extends AbstractItem implements SlotInterface{

	@Override
	public String getItemName() {
		return "精錬された除石具";
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return getLevel().getPrice();
	}

	@Override
	public String getSlotName() {
		return "remove_unavailable_slot";
	}

	@Override
	public String getSlotDetail() {
		return "使用不可のスロット1つ削除する";
	}

	@Override
	protected List<String> getAddDetail() {
		return Arrays.asList( "装着成功確率 : " + getLevel().getSucessPer() + "% ");
	}

	@Override
	public String getId() {
		return "remove_unavailable_slot";
	}

	@Override
	public ChatColor getNameColor() {
		return ChatColor.RED;
	}

	@Override
	public SlotType getSlotType() {
		return SlotType.REMOVE_UNAVAILABLE;
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.REMOVE_UNAVAILABLE;
	}

	@Override
	public boolean isSame(SlotInterface slot) {
		return slot.getClass().equals(AddEmptySlotItem.class);
	}

	@Override
	protected Material getMaterial() {
		return Material.SHEARS;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"鍛冶屋の魔法石装着画面で", "使用不可のスロットを１つ削除する"};
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
		return item;
	}
}
