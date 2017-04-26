package lbn.item.itemInterface;

import lbn.item.ItemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

public interface RightClickItemable extends ItemInterface {
  public void excuteOnRightClick(PlayerInteractEvent e);
}
