package net.l_bulb.dungeoncore.quest.questData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerQuestSessionManager {
  static HashMap<UUID, PlayerQuestSession> hashMap = new HashMap<UUID, PlayerQuestSession>();

  public static PlayerQuestSession getQuestSession(Player p) {
    if (!hashMap.containsKey(p.getUniqueId())) {
      hashMap.put(p.getUniqueId(), new PlayerQuestSession(p.getUniqueId()));
    }
    PlayerQuestSession playerQuestSession = hashMap.get(p.getUniqueId());
    return playerQuestSession;
  }

  public static void loadSession(Player p) {}

  public static void saveSession(Player p) {}
}
