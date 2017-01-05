package lbn.dungeon.contents.item.key;

import lbn.item.ItemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface KeyItemable extends ItemInterface{
	public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item);

	public String getLastLine(Player p, String[] params);
}
