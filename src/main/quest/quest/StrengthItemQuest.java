package main.quest.quest;

import java.util.HashMap;

import main.common.event.player.PlayerStrengthFinishEvent;
import main.common.event.quest.ComplateQuestEvent;
import main.quest.Quest;
import main.quest.QuestManager;

import org.bukkit.entity.Player;

public abstract class StrengthItemQuest extends AbstractVillagerQuest{

	static HashMap<String, StrengthItemQuest> questList = new HashMap<String, StrengthItemQuest>();

	public StrengthItemQuest() {
		init();
	}

	protected void init() {
		questList.put(getId(), this);
	}

	public static boolean isStrengthItemQuest(Quest q) {
		return questList.containsKey(q.getId());
	}

	public void onStrength(PlayerStrengthFinishEvent e) {
		if (isQuestComplate(e)) {
			QuestManager.complateQuest(this, e.getPlayer());
		}
	}

	abstract protected boolean isQuestComplate(PlayerStrengthFinishEvent e);

	@Override
	public void onComplate(ComplateQuestEvent e) {
	}

	@Override
	public String getCurrentInfo(Player p) {
		return "達成度(0/1)";
	}
}
