package lbn.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lbn.util.DungeonLogger;

public class PlayerData {
  static HashMap<UUID, Long> lastDeath = new HashMap<UUID, Long>();
  
  Player                     p;
  
  public PlayerData(Player p) {
    this.p = p;
  }
  
  public long getLastDeath() {
    try {
      if (lastDeath.containsKey(p.getUniqueId())) {
        return lastDeath.get(p.getUniqueId());
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
      DungeonLogger.error("lastDeathList is null:" + (lastDeath == null) + ", p is null" + (p == null));
    }
    return -1;
  }
  
  public static PlayerData getInstance(Player p) {
    return new PlayerData(p);
  }
  
  public static void onDeath(PlayerDeathEvent e) {
    lastDeath.put(e.getEntity().getUniqueId(), System.currentTimeMillis());
  }
  
  public static void disconnect(PlayerQuitEvent e) {
    UUID uniqueId = e.getPlayer().getUniqueId();
    lastDeath.remove(uniqueId);
  }
}
