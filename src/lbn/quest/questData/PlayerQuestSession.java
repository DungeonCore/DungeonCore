package lbn.quest.questData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import lbn.dungeoncore.SpletSheet.QuestSheetRunnable;
import lbn.quest.Quest;
import lbn.quest.quest.QuestType;
import lbn.util.JavaUtil;

import com.google.common.collect.HashMultimap;

public class PlayerQuestSession {
//	HashMultimap<QuestType, String> doingQuest = HashMultimap.create();
	HashMultimap<QuestType, Quest> doingQuest = HashMultimap.create();
	HashMap<Quest, ComplateData> complateQuest = new HashMap<>();

	HashMap<String, Integer> questData = new HashMap<>();

	long lastUpdate = -1;
	{
		lastUpdate = new QuestSheetRunnable(null).getLastUpdate();
	}

	public boolean isDoing(Quest q) {
		return doingQuest.containsValue(q);
	}

	public boolean isComplate(Quest q) {
		return complateQuest.containsKey(q);
	}

	public Set<Quest> getDoingQuestListByType(QuestType type) {
		return doingQuest.get(type);
	}

	public Collection<Quest> getDoingQuestList() {
		return doingQuest.values();
	}

	public Collection<Quest> getComplateQuestList() {
		return doingQuest.values();
	}

	/**
	 * クエストのデータ値を渡す
	 * @param q
	 * @return
	 */
	public int getQuestData(Quest q) {
		if (questData.containsKey(q.getId())) {
			return questData.get(q.getId());
		}
		return 0;
	}

	public void setQuestData(Quest q, int data) {
		questData.put(q.getId(), data);
	}

	public long getComplateDate(Quest q) {
		if (complateQuest.containsKey(q)) {
			return complateQuest.get(q).complateData;
		}
		return 0;
	}

	public long getComplateCount(Quest q) {
		if (complateQuest.containsKey(q)) {
			return complateQuest.get(q).complateCount;
		}
		return 0;
	}

	public void startQuest(Quest q) {
		doingQuest.put(q.getQuestType(), q);
	}

	public void removeQuest(Quest q) {
		doingQuest.remove(q.getQuestType(), q);
		questData.remove(q.getId());
	}

	public void complateQuest(Quest q) {
		ComplateData complateData = complateQuest.get(q);
		if (complateData == null) {
			complateData = new ComplateData();
		}
		//完了時間をセット
		complateData.complateCount = complateData.complateCount + 1;

		//完了時間をセット
		complateData.complateData = JavaUtil.getJapanTimeInMillis();
		complateQuest.put(q, complateData);

		//実行中から削除
		doingQuest.remove(q.getQuestType(), q);
		questData.remove(q.getId());
	}

	public int getNowQuestSize() {
		return doingQuest.values().size();
	}
}

class ComplateData {
	long complateData = 0;
	int complateCount = 0;
}
