package lbn.chest;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class AbstractCustomChest {
  public abstract String getName();

  public abstract boolean canOpen(Player p, Block block, PlayerInteractEvent e);

  public abstract void removeChest(Location loc);

  public abstract void open(Player p, Block block, PlayerInteractEvent e);

  public abstract void executeIfDebug(Player p, Block block, PlayerInteractEvent e);
}
