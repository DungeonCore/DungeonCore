package net.l_bulb.dungeoncore.common.event.player;

import lombok.Getter;

import org.bukkit.event.HandlerList;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

@Getter
public class PlayerChangeStatusLevelEvent extends TheLowPlayerEvent {
  private static final HandlerList handlers = new HandlerList();

  int level;
  LevelType leveltype;

  public PlayerChangeStatusLevelEvent(TheLowPlayer player, int level, LevelType leveltype) {
    super(player);
    this.level = level;
    this.leveltype = leveltype;
  }

  public int getNowExp() {
    return player.getExp(leveltype);
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}