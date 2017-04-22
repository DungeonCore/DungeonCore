package lbn.command;

import lbn.dungeoncore.SpletSheet.QuestSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.quest.QuestInventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		Player p = (Player) paramCommandSender;
		if (paramArrayOfString.length == 0) {
			QuestInventory.openQuestViewer(p);
			return true;
		}

		if (paramArrayOfString[0].equals("reload")) {
			QuestSheetRunnable questSheetRunnable = new QuestSheetRunnable(p);
			SpletSheetExecutor.onExecute(questSheetRunnable);
			return true;
		}
		return false;
	}

	public static void questReload() {
		QuestSheetRunnable questSheetRunnable = new QuestSheetRunnable(Bukkit.getConsoleSender());
		SpletSheetExecutor.onExecute(questSheetRunnable);
	}

}
