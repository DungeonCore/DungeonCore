package lbn.util;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.mob.abstractmob.villager.AbstractVillager;
import lbn.quest.QuestAnnouncement;

public class QuestUtil {
	public static void sendSatisfyComplateForVillager(String villagerName, Player p) {
		//TODO 音追加

		AbstractMob<?> mob = MobHolder.getMob(villagerName);
		if (mob == null && !(mob instanceof AbstractVillager)) {
			p.sendMessage("クエストクリア!!!  " + villagerName + "のところに戻ろう！！");
			return;
		}

		AbstractVillager villager = (AbstractVillager) mob;
		Location location = villager.getLocation();

		String loc;
		if (location != null) {
			loc = MessageFormat.format("{0}, {1}, {2}", location.getBlockX(), location.getBlockY(), location.getBlockZ());
		} else {
			loc = "現在は村人が存在しません";
		}

		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		String command = MessageFormat.format("tellraw {0} [\"\",{7}\"text\":\"{6}{1}\"},{7}\"text\":\"{2}\",\"hoverEvent\":{7}\"action\":\"show_text\",\"value\":\"Type : {3} ,Location : {4}\"}},{7}\"text\":\"{5}\"}]",
				p.getName(),
				"クエストクリア!!!  ",
				villager.getName(),
				villager.getEntityType(),
				loc,
				"のところに戻ろう",
				QuestAnnouncement.QUEST_INFO_PREFIX,
				"{"
				);
		Bukkit.dispatchCommand(consoleSender, command);
	}

	public static void sendMessageByVillager(Player p, String[] text) {
		if (text == null || text.length == 0) {
			return;
		}

		p.sendMessage("");
		for (String string : text) {
			p.sendMessage(ChatColor.GOLD + string);
		}
	}
}
