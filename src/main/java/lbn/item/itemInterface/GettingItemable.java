package lbn.item.itemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import lbn.item.ItemInterface;

public interface GettingItemable extends ItemInterface {
  public void onClickForGetting(PlayerInteractEvent e, String[] lines);

  public String getLastLine(Player p, String[] params);

}
