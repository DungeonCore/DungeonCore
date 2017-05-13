package net.l_bulb.dungeoncore.common.event;

import lombok.Getter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EndermanFindTargetEvent extends EntityEvent {
  @Getter
  Player target;

  public EndermanFindTargetEvent(Entity what, Player target) {
    super(what);
    this.target = target;
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
