package lbn.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.StringUtil;

import lbn.quest.Quest;
import lbn.quest.QuestInventory;
import lbn.quest.QuestManager;

public class CommandQuestOperate implements CommandExecutor, TabCompleter{
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		boolean isSelectPlayer = false;

		Player p = (Player) arg0;
		if (arg3.length >= 3) {
			p = Bukkit.getPlayer(arg3[arg3.length - 1]);
			if (p == null) {
				p = (Player) arg0;
			} else {
				isSelectPlayer = true;
			}
		}

		if (arg3.length == 0) {
			return false;
		}
		if (arg3[0].equals("removeVillages")) {
			onRemove(p);
			return true;
		} else if (arg3[0].equals("view")) {
			QuestInventory.openQuestViewer(p);
			return true;
		}

		if (arg3.length < 2) {
			return false;
		}

		String questName = StringUtils.join(Arrays.copyOfRange(arg3, 1, isSelectPlayer ? arg3.length - 1 : arg3.length), " ");

		Quest quest = null;
		//まずはIDから検索
		quest = QuestManager.getQuestById(questName);
		if (quest == null) {
			//次に名前を検索
			quest = QuestManager.getQuestByName(questName);
		}
		if (quest == null) {
			arg0.sendMessage("クエストが存在しません:" + questName);
			return true;
		}

		if (arg3[0].equals("start")) {
			QuestManager.startQuest(quest, p, false, QuestManager.getStartQuestStatus(quest, p));
		} else if (arg3[0].equals("remove")) {
			QuestManager.removeQuest(quest, p);
		} else if (arg3[0].equals("complate")) {
			QuestManager.complateQuest(quest, p);
		} else {
			arg0.sendMessage("不明な命令です:" + arg3[0]);
			return true;
		}

		return true;
	}

	private void onRemove(Player p) {
		List<Entity> nearbyEntities = p.getNearbyEntities(1, 1, 1);
		for (Entity entity : nearbyEntities) {
			if (entity instanceof Villager) {
				entity.remove();
			}
		}
	}

	static HashSet<String> operateList = new HashSet<String>();
	static{
		operateList.add("start");
		operateList.add("startById");
		operateList.add("remove");
		operateList.add("complate");
		operateList.add("removeVillages");
		operateList.add("view");
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], operateList, new ArrayList<String>(operateList.size()));
		} else if (arg3.length == 2) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[1], QuestManager.getQuestId(), new ArrayList<String>(QuestManager.getQuestId().size()));
		}
		return null;
	}

}
