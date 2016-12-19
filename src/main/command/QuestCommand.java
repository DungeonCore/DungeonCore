package main.command;

import main.quest.Quest;
import main.quest.QuestInventory;
import main.quest.QuestManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		Player p = (Player) paramCommandSender;
		if (paramArrayOfString.length == 0) {
			QuestInventory.openQuestViewer(p);
			return true;
		}

		if (paramArrayOfString.length == 2 && paramArrayOfString[0].equals("start")) {
			Quest q = QuestManager.getQuestById(paramArrayOfString[1]);
			if (q == null) {
				return false;
			}

			if (QuestManager.canStartQuestByTellrow(q, p)) {
				QuestManager.startQuest(q, p, false);
			}
		}
		return true;
	}

}
