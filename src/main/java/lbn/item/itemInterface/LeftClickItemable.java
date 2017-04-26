package lbn.item.itemInterface;

import lbn.item.ItemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

public interface LeftClickItemable extends ItemInterface {
  public void excuteOnLeftClick(PlayerInteractEvent e);
}
