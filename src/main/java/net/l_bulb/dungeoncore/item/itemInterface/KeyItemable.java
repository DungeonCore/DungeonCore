package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface KeyItemable extends ItemInterface {
  public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item);

  public String getLastLine(Player p, String[] params);
}
