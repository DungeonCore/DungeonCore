package net.l_bulb.dungeoncore.common.event.player;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;

import org.bukkit.event.HandlerList;

public class PlayerChangeStatusExpEvent extends TheLowPlayerEvent {
  private static final HandlerList handlers = new HandlerList();

  int addExp;
  LevelType levelType;
  StatusAddReason reason;

  public PlayerChangeStatusExpEvent(TheLowPlayer p, int addExp, LevelType levelType, StatusAddReason reason) {
    super(p);
    this.addExp = addExp;
    this.levelType = levelType;
    this.reason = reason;
  }

  public StatusAddReason getReason() {
    return reason;
  }

  public int getAddExp() {
    return addExp;
  }

  public LevelType getLevelType() {
    return levelType;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
