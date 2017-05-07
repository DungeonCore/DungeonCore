package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface RightClickItemable extends ItemInterface {
  public void excuteOnRightClick(PlayerInteractEvent e);
}
