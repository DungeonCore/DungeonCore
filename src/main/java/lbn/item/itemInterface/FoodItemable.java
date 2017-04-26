package lbn.item.itemInterface;

import org.bukkit.event.player.PlayerItemConsumeEvent;

import lbn.item.ItemInterface;

public interface FoodItemable extends ItemInterface {
  void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event);
}
