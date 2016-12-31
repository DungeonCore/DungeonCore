package lbn.money.galion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lbn.common.event.player.PlayerChangeGalionsEvent;
import lbn.common.other.SystemLog;
import lbn.player.playerIO.PlayerIODataManager;

public class GalionManager {

	// Galion を複数スレッドから排他制御する場合に必要
	private static Map<Player, Integer> galionMap = new ConcurrentHashMap<Player, Integer>();

	public static void setGalion(Player p, int val, GalionEditReason reason) {
		remove(p);
		addGalion(p, val, reason);
	}

	public static void addGalion(Player p, int val, GalionEditReason reason) {
		if (!galionMap.containsKey(p)) {
			galionMap.put(p, 0);
		}

		PlayerChangeGalionsEvent event = new PlayerChangeGalionsEvent(p, val, reason);
		Bukkit.getServer().getPluginManager().callEvent(event);

		int money = galionMap.get(p);
		money += event.getGalions();
		money = Math.max(money, 0);
		galionMap.put(p, money);

		// SQLに挿入
		// PlayerDataSql playerDataSql = new PlayerDataSql(p);
		// playerDataSql.setGalions(val);

		printLog(p, val, reason);

		PlayerIODataManager.save(p);
	}

	public static int getGalion(Player p) {
		if (!galionMap.containsKey(p)) {
			galionMap.put(p, 0);
		}
		return galionMap.get(p);
	}

	public static void remove(Player p) {
		PlayerIODataManager.save(p);
		// galionMap.remove(p);
	}

	public static void printLog(Player p, int val, GalionEditReason reason) {
		if (reason != GalionEditReason.mob_drop) {
			final String format = "%s get %d galions by %s, (total %d galions)";
			final String message = String.format(format, p.getName(), val, reason.toString(), galionMap.get(p));
			SystemLog.addLog(message);
		}
	}
}
