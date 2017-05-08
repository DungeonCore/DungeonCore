package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.item.ItemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

public interface RightClickItemable extends ItemInterface {
  public void excuteOnRightClick(PlayerInteractEvent e);
}
