package lbn.common.event.player;

import org.bukkit.event.HandlerList;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.player.status.StatusAddReason;

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
