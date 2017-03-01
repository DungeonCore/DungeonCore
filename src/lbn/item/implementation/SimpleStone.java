package lbn.item.implementation;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.AbstractItem;

public class SimpleStone extends AbstractItem{

	@Override
	public String getItemName() {
		return "石ころ";
	}

	@Override
	public String getId() {
		return "simple_stone";
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 10;
	}

	@Override
	protected Material getMaterial() {
		return Material.COBBLESTONE;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"ただの石ころ"};
	}

}
