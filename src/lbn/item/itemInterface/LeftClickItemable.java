package lbn.item.itemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

import lbn.item.ItemInterface;

public interface LeftClickItemable extends ItemInterface {
	public void excuteOnLeftClick(PlayerInteractEvent e);
}
