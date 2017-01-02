package lbn.quest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestAnnouncement {
	public static final String QUEST_INFO_PREFIX = ChatColor.GOLD + "[Quest]" + ChatColor.GREEN;

	public static void sendQuestProcessInfo(Player p, String msg) {
		p.sendMessage(QUEST_INFO_PREFIX + msg);
	}
}
