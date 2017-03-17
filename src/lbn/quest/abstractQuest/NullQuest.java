package lbn.quest.abstractQuest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lbn.common.event.quest.StartQuestEvent;
import lbn.quest.Quest;

import org.bukkit.entity.Player;

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
	public String[] getQuestDetail() {
		return new String[]{"クエストが存在しません"};
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
	public String[] getTalkOnStart() {
		return null;
	}

	@Override
	public String[] getTalkOnComplate() {
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
	public String getEndVillagerId() {
		return null;
	}

	@Override
	public String getComplateCondition() {
		return "クエスト未登録";
	}

	@Override
	public void onStartQuestEvent(StartQuestEvent e) {
	}
}