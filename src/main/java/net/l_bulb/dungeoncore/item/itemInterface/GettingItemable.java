package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface GettingItemable extends ItemInterface {
  public void onClickForGetting(PlayerInteractEvent e, String[] lines);

  public String getLastLine(Player p, String[] params);

}
