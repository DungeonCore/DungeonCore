package lbn.dungeon.contents.item.key;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;

public interface KeyItemable extends ItemInterface{
	public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item);

	public String getLastLine(Player p, String[] params);
}
