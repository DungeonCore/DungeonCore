package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.item.ItemInterface;

import org.bukkit.event.player.PlayerItemConsumeEvent;

public interface FoodItemable extends ItemInterface {
  void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event);
}
