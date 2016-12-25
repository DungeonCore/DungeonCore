package lbn.quest.quest;

import java.util.Set;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.quest.questData.PlayerQuestSession;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class TouchVillagerQuest extends AbstractVillagerQuest{
	static HashMultimap<String, TouchVillagerQuest> targetVillagerNameQuestList = HashMultimap.create();

	String villagerName;
	String[] talk;

	private TouchVillagerQuest(String id, String name, String[] talk) {
		super(id);
		name = villagerName;
		this.talk = talk;
		targetVillagerNameQuestList.put(getTargetVillagerName().toLowerCase(), this);
	}

	public String[] talkOnTouch() {
		return talk;
	}

	public static TouchVillagerQuest getInstance(String id, String data1, String data2) {
		if (data1 == null || data2 == null) {
			return null;
		}
		return new TouchVillagerQuest(id, data1, data2.split(","));
	}

	public static Set<TouchVillagerQuest> getQuestByTargetVillager(LivingEntity e) {
		String name = e.getCustomName().toLowerCase();
		return targetVillagerNameQuestList.get(name);
	}

	public void onTouchVillager(Player p, LivingEntity entity, PlayerQuestSession session) {
		String name = ((LivingEntity)entity).getCustomName();
		if (name.equalsIgnoreCase(getTargetVillagerName())) {
			session.setQuestData(this, 1);
		}
	}

	public String getTargetVillagerName() {
		return villagerName;
	}

	@Override
	public String getCurrentInfo(Player p) {
		return "達成度(0/1)";
	}

	@Override
	public void onComplate(ComplateQuestEvent e) {
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.TOUCH_VILLAGER_QUEST;
	}

	@Override
	public boolean isComplate(int data) {
		return data == 1;
	}

	@Override
	public void onStart(StartQuestEvent e) {
	}

	@Override
	public void onDistruction(DestructionQuestEvent e) {

	}

	@Override
	protected void init() {

	}
}
