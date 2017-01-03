package lbn.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lbn.mob.mob.abstractmob.villager.AbstractVillager;

import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class AvailbleQuestHolder {
	static HashMultimap<String, Quest> villagerNameQuestMap = HashMultimap.create();

	public static void regist(Quest q) {
		villagerNameQuestMap.put(q.getEndVillagerName(), q);
	}

	public static List<Quest> getAvailableQuestList(AbstractVillager v, Player p) {
		ArrayList<Quest> availableQuestList = new ArrayList<Quest>();

		Set<Quest> set = villagerNameQuestMap.get(v.getName());
		for (Quest quest : set) {
			if (QuestManager.getStartQuestStatus(quest, p).canStart()) {
				availableQuestList.add(quest);
			}
		}

		return availableQuestList;
	}
}
