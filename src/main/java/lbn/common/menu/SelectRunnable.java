package lbn.common.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SelectRunnable {
	public void run(Player p, ItemStack item);
}
