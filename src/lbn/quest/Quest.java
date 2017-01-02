package lbn.quest;

import java.util.List;
import java.util.Set;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.quest.quest.QuestType;

import org.bukkit.entity.Player;

public interface Quest{
	public String getId();

	public String getName();

	public String getQuestDetail();

	public void onStart(StartQuestEvent e);

	public void onComplate(ComplateQuestEvent e);

	public void onDistruction(DestructionQuestEvent e);

	public Set<Quest> getBeforeQuest();

	public boolean isMainQuest();

	public boolean isStartOverlap();

	public boolean canDestory();

	public void onSatisfyComplateCondtion(Player p);

	public String getCurrentInfo(Player p);

	public void playCompleteSound(Player p);

	public void playDistructionSound(Player p);

	public void playStartSound(Player p);

	public boolean isNullQuest();

	public QuestType getQuestType();

	public boolean isComplate(int data);

	/**
	 * 受けた後の会話
	 * @return
	 */
	public String[] getTalk1();

	/**
	 * 完了した後の会話
	 * @return
	 */
	public String[] getTalk2();

	public boolean isShowTitle();

	public Quest getAutoExecuteNextQuest();

	public long getCoolTimeSecound();

	public void giveRewardItem(Player p);

	public boolean canGetRewordItem(Player p);

	public int getAvailableMainLevel();

	public List<String> getRewordText();

	public String getStartVillagerName();

	public String getEndVillagerName();

	public QuestProcessingStatus getProcessingStatus(Player p);
}
