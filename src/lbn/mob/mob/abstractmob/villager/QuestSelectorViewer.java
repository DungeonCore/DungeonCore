package lbn.mob.mob.abstractmob.villager;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.QuestManager.QuestStartStatus;

public class QuestSelectorViewer {
	public static void openSelector(QuestVillager villager, Player p) {
		//実行可能クエスト
		ArrayList<Quest> canStartQuestList = new ArrayList<Quest>();
		for (Quest quest : villager.getHaveQuest()) {
			//Questが実行可能か調べる
			QuestStartStatus startQuestStatus = QuestManager.getStartQuestStatus(quest, p);
			if (startQuestStatus.canStart()) {
				canStartQuestList.add(quest);
			}
		}

		//実行可能クエストがないなら何もしない
		if (canStartQuestList.isEmpty()) {
			return;
		}
		//TODO クエストセレクターを開く
	}
}
