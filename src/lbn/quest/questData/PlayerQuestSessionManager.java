package lbn.quest.questData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerQuestSessionManager {
	static HashMap<UUID, PlayerQuestSession> hashMap = new HashMap<UUID, PlayerQuestSession>();

	public static PlayerQuestSession getQuestSession(Player p) {
		PlayerQuestSession playerQuestSession = hashMap.get(p.getUniqueId());
		if (playerQuestSession == null) {
			loadSession(p);
			return new PlayerQuestSession();
		}
		return playerQuestSession;
	}

	public static void loadSession(Player p) {
		//TODO
	}

	public static void saveSession(Player p) {
		//TODO
	}
}
