package main.dungeon.contents.item.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import main.item.AbstractItem;

public class CaveStone extends AbstractItem {
	@Override
	public String getItemName() {
		return "CAVE STONE";
	}

	@Override
	protected Material getMaterial() {
		return Material.COBBLESTONE;
	}

	@Override
	protected String[] getDetail() {
		return new String[]{"洞窟にあるただの石"};
	}

	@Override
	public String getId() {
		return "cave stone";
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 10;
	}

	@Override
	public boolean isDispList() {
		return false;
	}

}
