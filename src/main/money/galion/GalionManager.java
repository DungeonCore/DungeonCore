package main.money.galion;

import java.util.HashMap;

import main.common.event.player.PlayerChangeGalionsEvent;
import main.common.other.SystemLog;
import main.player.playerIO.PlayerIODataManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GalionManager {
	static HashMap<Player, Integer> galionMap = new HashMap<Player, Integer>();

	public static void setGalion(Player p, int val, GalionEditReason reason) {
		remove(p);
		addGalion(p, val, GalionEditReason.system);
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

		//SQLに挿入
//		PlayerDataSql playerDataSql = new PlayerDataSql(p);
//		playerDataSql.setGalions(val);

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
//		galionMap.remove(p);
	}

	public static void printLog(Player p, int val, GalionEditReason reason) {
		if (reason != GalionEditReason.mob_drop) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(p.getName());
			stringBuilder.append(" get ");
			stringBuilder.append(val);
			stringBuilder.append("galions by ");
			stringBuilder.append(reason);
			stringBuilder.append(", (total ");
			stringBuilder.append(galionMap.get(p));
			stringBuilder.append(" galions)");
			SystemLog.addLog(stringBuilder.toString());
		}
	}
}
