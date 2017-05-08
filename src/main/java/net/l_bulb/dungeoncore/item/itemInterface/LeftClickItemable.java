package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.item.ItemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

public interface LeftClickItemable extends ItemInterface {
  public void excuteOnLeftClick(PlayerInteractEvent e);
}
