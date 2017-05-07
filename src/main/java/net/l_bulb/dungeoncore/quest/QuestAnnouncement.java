package net.l_bulb.dungeoncore.quest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestAnnouncement {
  public static final String QUEST_INFO_PREFIX = ChatColor.GOLD + "[Quest]" + ChatColor.GREEN;
  public static final String QUEST_ERROR_PREFIX = ChatColor.GOLD + "[Quest]" + ChatColor.RED;

  public static void sendQuestProcessInfo(Player p, String msg) {
    if (p != null) {
      p.sendMessage(QUEST_INFO_PREFIX + msg);
    }
  }

  public static void sendQuestError(Player p, String msg) {
    if (p != null) {
      p.sendMessage(QUEST_ERROR_PREFIX + msg);
    }
  }
}
