package main.chest.wireless;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import main.chest.AbstractCustomChest;

public abstract class WireLessChest extends AbstractCustomChest{
	abstract public Location getContainsLocation(Player p, Block block, PlayerInteractEvent e);

	@Override
	public void open(Player p, Block block, PlayerInteractEvent e) {
		Location containsLocation = getContainsLocation(p, block, e);
		if (containsLocation == null) {
			return;
		}

		if (containsLocation.getBlock().getState() instanceof Chest) {
			Chest c = (Chest) containsLocation.getBlock().getState();
			p.openInventory(c.getBlockInventory());
		}
	}

	@Override
	public void executeIfDebug(Player p, Block block, PlayerInteractEvent e) {
	}

	@Override
	public void removeChest(Location loc) {
	}
}
