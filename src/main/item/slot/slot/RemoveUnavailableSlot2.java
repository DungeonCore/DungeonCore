package main.item.slot.slot;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import main.item.slot.SlotLevel;

public class RemoveUnavailableSlot2 extends RemoveUnavailableSlot{
	@Override
	public String getItemName() {
		return "除石具";
	}

	@Override
	public String getId() {
		return super.getId() + "2";
	}

	@Override
	public SlotLevel getLevel() {
		return SlotLevel.REMOVE_UNAVAILABLE2;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
		return item;
	}
}
