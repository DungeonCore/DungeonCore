package net.l_bulb.dungeoncore.common.event.player;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.money.GalionEditReason;

import org.bukkit.event.HandlerList;

public class PlayerChangeGalionsEvent extends TheLowPlayerEvent {

  int val;
  GalionEditReason reason;

  public PlayerChangeGalionsEvent(TheLowPlayer who, int val, GalionEditReason reason) {
    super(who);
    this.reason = reason;
    this.val = val;
  }

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public int getAddGalions() {
    return val;
  }

  public GalionEditReason getReason() {
    return reason;
  }

  public void setGalions(int val) {
    this.val = val;
  }

  public void setReason(GalionEditReason reason) {
    this.reason = reason;
    ;
  }
}
