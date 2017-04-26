package lbn.common.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerJoinDungeonGameEvent extends PlayerEvent {
  private static final HandlerList handlers = new HandlerList();

  public PlayerJoinDungeonGameEvent(Player who) {
    super(who);
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public static boolean inDungeon(Player p) {
    return true;
  }
}
