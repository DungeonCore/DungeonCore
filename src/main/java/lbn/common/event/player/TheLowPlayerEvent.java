package lbn.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import lbn.api.player.TheLowPlayer;

public abstract class TheLowPlayerEvent extends Event {
  TheLowPlayer player;

  public TheLowPlayerEvent(TheLowPlayer player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player.getOnlinePlayer();
  }

  public OfflinePlayer getOfflinePlayer() {
    return player.getOfflinePlayer();
  }

  public TheLowPlayer getTheLowPlayer() {
    return player;
  }

  public void callEvent() {
    Bukkit.getServer().getPluginManager().callEvent(this);
  }

  public boolean isOnline() {
    return player.getOfflinePlayer().isOnline();
  }
}
