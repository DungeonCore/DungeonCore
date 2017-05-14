package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

public class PlayerLoadedDataEvent extends TheLowPlayerEvent {

  boolean isSuccess;

  Player onlinePlayer;
  OfflinePlayer offlinePlayer;

  public PlayerLoadedDataEvent(TheLowPlayer player, OfflinePlayer p) {
    super(player);
    this.isSuccess = (player != null);

    this.offlinePlayer = p;
    if (p instanceof Player) {
      onlinePlayer = (Player) p;
    }
  }

  @Override
  public Player getPlayer() {
    return onlinePlayer;
  }

  @Override
  public OfflinePlayer getOfflinePlayer() {
    return offlinePlayer;
  }

  /**
   * ロードに成功したならTRUE
   *
   * @return
   */
  public boolean isSuccess() {
    return isSuccess;
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
