package main.quest;

import java.util.List;

import main.common.event.quest.ComplateQuestEvent;
import main.common.event.quest.DestructionQuestEvent;
import main.common.event.quest.StartQuestEvent;

import org.bukkit.entity.Player;

public interface Quest{
	public String getId();

	public String getName();

	public String getQuestDetail();

	public void onStart(StartQuestEvent e);

	public void onComplate(ComplateQuestEvent e);

	public void onDistruction(DestructionQuestEvent e);

	public List<Quest> getBeforeQuest();

	public boolean isMainQuest();

	public boolean isStartOverlap();

	public boolean canDestory();

	public String getCurrentInfo(Player p);

	public void playCompleteSound(Player p);

	public void playDistructionSound(Player p);

	public void playStartSound(Player p);

	public boolean isDoing(Player p);

	public String[] getTalk1();

	public String[] getTalk2();

	public void giveRewardItem(Player p);
}
