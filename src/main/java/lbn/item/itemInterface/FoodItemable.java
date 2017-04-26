package lbn.item.itemInterface;

import lbn.item.ItemInterface;

import org.bukkit.event.player.PlayerItemConsumeEvent;

public interface FoodItemable extends ItemInterface {
  void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event);
}
