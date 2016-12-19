package lbn.quest;

import java.util.List;

import org.bukkit.entity.Player;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;

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
