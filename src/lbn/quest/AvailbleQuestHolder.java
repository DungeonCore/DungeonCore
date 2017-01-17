package lbn.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lbn.npc.VillagerNpc;

import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class AvailbleQuestHolder {
	static HashMultimap<String, Quest> villagerNameQuestMap = HashMultimap.create();

	public static void regist(Quest q) {
		villagerNameQuestMap.put(q.getEndVillagerName(), q);
	}

	public static List<Quest> getAvailableQuestList(VillagerNpc npc, Player p) {
		ArrayList<Quest> availableQuestList = new ArrayList<Quest>();

		Set<Quest> set = villagerNameQuestMap.get(npc.getName());
		for (Quest quest : set) {
			if (QuestManager.getStartQuestStatus(quest, p).canStart()) {
				availableQuestList.add(quest);
			}
		}

		return availableQuestList;
	}
}
