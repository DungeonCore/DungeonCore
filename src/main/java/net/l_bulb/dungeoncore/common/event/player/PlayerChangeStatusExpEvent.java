package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;

@Getter
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

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
