package net.l_bulb.dungeoncore.common.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EndermanFindTargetEvent extends EntityEvent {
  Player p;

  public EndermanFindTargetEvent(Entity what, Player p) {
    super(what);
    this.p = p;
  }

  public Player getTarget() {
    return p;
  }

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
