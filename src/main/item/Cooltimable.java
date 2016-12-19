package main.item;

import org.bukkit.inventory.ItemStack;

public interface Cooltimable {
	int getCooltimeTick(ItemStack item);

	String getId();
}
