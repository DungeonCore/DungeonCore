package lbn.quest.quest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.quest.Quest;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;

public class NullQuest implements Quest{

	String id;

	public NullQuest(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public String getQuestDetail() {
		return "クエストが存在しません";
	}

	@Override
	public void onStart(StartQuestEvent e) {

	}

	@Override
	public void onComplate(ComplateQuestEvent e) {

	}

	@Override
	public void onDistruction(DestructionQuestEvent e) {

	}

	@Override
	public Set<Quest> getBeforeQuest() {
		return null;
	}

	@Override
	public boolean isMainQuest() {
		return false;
	}

	@Override
	public boolean isStartOverlap() {
		return false;
	}

	@Override
	public String getCurrentInfo(Player p) {
		return null;
	}

	@Override
	public void playCompleteSound(Player p) {

	}

	@Override
	public void playDistructionSound(Player p) {

	}

	@Override
	public void playStartSound(Player p) {

	}

	@Override
	public boolean canDestory() {
		return false;
	}

	@Override
	public String[] getTalk1() {
		return null;
	}

	@Override
	public String[] getTalk2() {
		return null;
	}

	@Override
	public void giveRewardItem(Player p) {
	}

	@Override
	public Quest getAutoExecuteNextQuest() {
		return null;
	}

	@Override
	public boolean isShowTitle() {
		return false;
	}

	@Override
	public long getCoolTimeSecound() {
		return 0;
	}

	@Override
	public boolean canGetRewordItem(Player p) {
		return false;
	}

	@Override
	public boolean isNullQuest() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Quest) {
			return getId().equals(((Quest) obj).getId());
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.UnknownQuest;
	}

	@Override
	public boolean isComplate(int data) {
		return false;
	}

	@Override
	public void onSatisfyComplateCondtion(Player p) {

	}

	@Override
	public int getAvailableMainLevel() {
		return 0;
	}

	@Override
	public List<String> getRewordText() {
		return Arrays.asList("なし");
	}

	@Override
	public String getStartVillagerName() {
		return null;
	}

	@Override
	public String getEndVillagerName() {
		return null;
	}

	@Override
	public QuestProcessingStatus getProcessingStatus(Player p) {
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		if (questSession.isDoing(this)) {
			return QuestProcessingStatus.PROCESSING;
		} else {
			return QuestProcessingStatus.NOT_START;
		}
	}

}