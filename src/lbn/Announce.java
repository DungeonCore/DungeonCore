package lbn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Announce {
	public static void AnnounceInfo(String msg) {
		Bukkit.broadcastMessage(ChatColor.GREEN  + "[THELoW]:" + ChatColor.AQUA +  msg);
	}
}
