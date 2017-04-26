package lbn.common.event.player;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;

import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends TheLowPlayerEvent {

  int newLevel;
  LevelType levelType;

  public PlayerLevelUpEvent(TheLowPlayer player, LevelType type) {
    super(player);
    this.newLevel = player.getLevel(type);
    this.levelType = type;
  }

  public int getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(int newLevel) {
    this.newLevel = newLevel;
  }

  public LevelType getLevelType() {
    return levelType;
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
