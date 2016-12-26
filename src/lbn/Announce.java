package lbn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Announce {
	public static void AnnounceInfo(String msg) {
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Announce" + ChatColor.DARK_GRAY + "]:" + ChatColor.AQUA + msg);
	}

	public static void AttentionInfo(String msg) {
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Attention!" + ChatColor.DARK_GRAY + "]:" + ChatColor.WHITE + msg);
	}
}
