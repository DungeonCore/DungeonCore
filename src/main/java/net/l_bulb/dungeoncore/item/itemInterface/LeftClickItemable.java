package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface LeftClickItemable extends ItemInterface {
  public void excuteOnLeftClick(PlayerInteractEvent e);
}
