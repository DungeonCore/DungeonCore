package main.item.itemInterface;

import main.item.ItemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

public interface RightClickItemable extends ItemInterface{
	public void excuteOnRightClick(PlayerInteractEvent e);
}
