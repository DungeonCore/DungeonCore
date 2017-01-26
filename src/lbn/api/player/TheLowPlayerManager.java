package lbn.api.player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.player.PlayerEvent;

import lbn.player.player.CustomPlayer;

public class TheLowPlayerManager {
	static ConcurrentHashMap<UUID, TheLowPlayer> loadedPlayerMap = new ConcurrentHashMap<UUID, TheLowPlayer>();

	static Set<OfflinePlayer> loadingNow = Collections.synchronizedSet(new HashSet<OfflinePlayer>());

	public static void loadData(OfflinePlayer p) {
		//もし情報取得中なら無視する
		if (loadingNow.contains(p)) {
			return;
		}
		//TODO ロードする
		CustomPlayer customPlayer = new CustomPlayer(p);
		customPlayer.init();
		loadedPlayerMap.put(p.getUniqueId(), customPlayer);
	}

	public static void saveData(OfflinePlayer p) {
		TheLowPlayer theLowPlayer = getTheLowPlayer(p);
		if (theLowPlayer == null) {
			return;
		}
		//TODO データをセーブする
	}

	/**
	 * THELoW Playerを取得, ロードされていない場合はNullを返す
	 * @param p
	 * @return
	 */
	public static TheLowPlayer getTheLowPlayer(OfflinePlayer p) {
		return getTheLowPlayer(p.getUniqueId());
	}

	/**
	 * THELoW Playerを取得, ロードされていない場合はNullを返す
	 * @param id
	 * @return
	 */
	public static TheLowPlayer getTheLowPlayer(UUID id) {
		return loadedPlayerMap.get(id);
	}

	/**
	 * THELoW Playerを取得, ロードされていない場合はNullを返す
	 * @param id
	 * @return
	 */
	public static TheLowPlayer getTheLowPlayer(PlayerEvent event) {
		return getTheLowPlayer(event.getPlayer());
	}

	/**
	 *ロードしていたらTRUE
	 * @return
	 */
	public static boolean isLoaded(OfflinePlayer p) {
		return loadedPlayerMap.containsKey(p);
	}
}
