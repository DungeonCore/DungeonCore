package lbn.item.SpreadSheetItem;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.AbstractItem;
import lbn.util.ItemStackUtil;

public class SpreadSheetOtherItem extends AbstractItem {
	public SpreadSheetOtherItem(String name, String id, int price,
			String command) {
		super();
		this.name = name;
		this.id = id;
		this.price = price;
		this.command = command;

		ItemStack itemStackByCommand = ItemStackUtil.getItemStackByCommand(command);
		m = itemStackByCommand.getType();
		lore = ItemStackUtil.getLore(itemStackByCommand);
	}

	String name;
	String id;
	int price;
	Material m;
	List<String> lore;
	String command;

	@Override
	public String getItemName() {
		return name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return price;
	}

	@Override
	protected ItemStack getItemStackBase() {
		ItemStack itemStack = ItemStackUtil.getItemStackByCommand(command);
		return itemStack;
	}

	@Override
	protected Material getMaterial() {
		return m;
	}

	@Override
	protected String[] getDetail() {
		return lore.toArray(new String[0]);
	}
}
