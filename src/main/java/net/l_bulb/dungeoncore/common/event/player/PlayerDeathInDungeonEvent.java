package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerDeathInDungeonEvent extends PlayerEvent {

  public PlayerDeathInDungeonEvent(Player player) {
    super(player);
  }

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public void callEvent() {
    Bukkit.getServer().getPluginManager().callEvent(this);
  }
}
