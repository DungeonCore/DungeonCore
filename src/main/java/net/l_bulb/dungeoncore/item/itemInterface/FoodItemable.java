package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.player.PlayerItemConsumeEvent;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface FoodItemable extends ItemInterface {
  void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event);
}
