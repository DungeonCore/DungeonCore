package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.money.GalionEditReason;
@Getter
@Setter
public class PlayerChangeGalionsEvent extends TheLowPlayerEvent {
  int Galions;
  GalionEditReason reason;

  public PlayerChangeGalionsEvent(TheLowPlayer who, int Galions, GalionEditReason reason) {
    super(who);
    this.reason = reason;
    this.Galions = Galions;
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
