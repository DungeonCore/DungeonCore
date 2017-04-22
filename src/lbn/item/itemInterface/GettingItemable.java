package lbn.item.itemInterface;

import lbn.item.ItemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface GettingItemable extends ItemInterface {
	public void onClickForGetting(PlayerInteractEvent e, String[] lines);

	public String getLastLine(Player p, String[] params);

}
