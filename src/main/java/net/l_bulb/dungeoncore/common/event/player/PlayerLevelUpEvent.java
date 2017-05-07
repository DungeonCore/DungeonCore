package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

@Getter
public class PlayerLevelUpEvent extends TheLowPlayerEvent {
  @Setter
  int newLevel;

  LevelType levelType;

  public PlayerLevelUpEvent(TheLowPlayer player, LevelType type) {
    super(player);
    this.newLevel = player.getLevel(type);
    this.levelType = type;
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
