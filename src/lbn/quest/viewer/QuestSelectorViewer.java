package lbn.quest.viewer;

import java.util.List;

import lbn.npc.VillagerNpc;
import lbn.quest.AvailbleQuestHolder;
import lbn.quest.Quest;

import org.bukkit.entity.Player;

public class QuestSelectorViewer {
	public static void openSelector(VillagerNpc villager, Player p) {
		//実行可能クエスト
		List<Quest> canStartQuestList = AvailbleQuestHolder.getAvailableQuestList(villager, p);

		//実行可能クエストがないなら何もしない
		if (canStartQuestList.isEmpty()) {
			return;
		}
		QuestMenuSelector questMenuSelector = new QuestMenuSelector(canStartQuestList);
		questMenuSelector.open(p);
	}
}

