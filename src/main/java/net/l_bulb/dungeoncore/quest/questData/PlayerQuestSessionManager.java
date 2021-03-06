package net.l_bulb.dungeoncore.quest.questData;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.util.InOutputUtil;

public class PlayerQuestSessionManager {
  static HashMap<UUID, PlayerQuestSession> hashMap = new HashMap<>();

  public static PlayerQuestSession getQuestSession(Player p) {
    if (!hashMap.containsKey(p.getUniqueId())) {
      hashMap.put(p.getUniqueId(), new PlayerQuestSession(p.getUniqueId()));
    }
    PlayerQuestSession playerQuestSession = hashMap.get(p.getUniqueId());
    return playerQuestSession;
  }

  public static void loadSession(Player p) {
    // すでにロードされているなら何もしない
    if (hashMap.containsKey(p.getUniqueId())) { return; }
    // ファイルからロードする
    Object inputStream = InOutputUtil.deserializeObject("quest" + File.separator + p.getUniqueId() + ".dat");
    if (inputStream == null) {
      hashMap.put(p.getUniqueId(), new PlayerQuestSession(p.getUniqueId()));
      return;
    }
    try {
      hashMap.put(p.getUniqueId(), (PlayerQuestSession) inputStream);
    } catch (Exception e) {
      hashMap.put(p.getUniqueId(), new PlayerQuestSession(p.getUniqueId()));
    }
  }

  public static void saveSession(Player p) {
    PlayerQuestSession serializable = hashMap.get(p.getUniqueId());
    if (serializable == null) { return; }
    InOutputUtil.serializeObject(serializable, "quest" + File.separator + p.getUniqueId() + ".dat");
  }
}
