package lbn.quest.quest;

import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.google.common.collect.HashMultimap;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.quest.QuestManager;

public abstract class TouchVillagerQuest extends AbstractVillagerQuest{
	static HashMultimap<String, TouchVillagerQuest> targetVillagerNameQuestList = HashMultimap.create();

	public TouchVillagerQuest() {
		init();
	}

	protected void init() {
		if (getTargetVillagerName() != null) {
			targetVillagerNameQuestList.put(getTargetVillagerName().toLowerCase(), this);
		}
	}

	public static Set<TouchVillagerQuest> getQuestByTargetVillager(Villager e) {
		String name = e.getCustomName().toLowerCase();
		return targetVillagerNameQuestList.get(name);
	}

	public void onTouchVillager(Player p, LivingEntity entity) {
		if (entity.getType() != EntityType.VILLAGER) {
			return;
		}
		String name = ((Villager)entity).getCustomName();
		if (name.equalsIgnoreCase(getTargetVillagerName())) {
			QuestManager.complateQuest(this, p);
		}
	}

	abstract public String getTargetVillagerName();

	@Override
	public String getCurrentInfo(Player p) {
		return "達成度(0/1)";
	}

	@Override
	public void onComplate(ComplateQuestEvent e) {
	}
}
