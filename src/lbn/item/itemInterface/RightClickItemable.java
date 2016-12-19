package lbn.item.itemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

import lbn.item.ItemInterface;

public interface RightClickItemable extends ItemInterface{
	public void excuteOnRightClick(PlayerInteractEvent e);
}
