package lbn;

import static java.lang.String.*;
import static org.bukkit.Bukkit.*;
import static org.bukkit.ChatColor.*;

public final class Announce {

	private static final String FORMAT_ANNOUNCE = format("%s[%sAnnounce%s]:%s%%s", DARK_GRAY, GREEN, DARK_GRAY, AQUA);
	private static final String FORMAT_ATTENTION = format("%s[%sAttention!%s]:%s%%s", DARK_GRAY, DARK_RED, DARK_GRAY, WHITE);

	public static void announce(String msg) {
		broadcastMessage(format(FORMAT_ANNOUNCE, msg));
	}

	public static void attention(String msg) {
		broadcastMessage(format(FORMAT_ATTENTION, msg));
	}

}
