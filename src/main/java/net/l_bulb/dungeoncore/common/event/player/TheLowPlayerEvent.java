package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import lombok.AllArgsConstructor;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

@AllArgsConstructor
public abstract class TheLowPlayerEvent extends Event {
  TheLowPlayer player;

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
