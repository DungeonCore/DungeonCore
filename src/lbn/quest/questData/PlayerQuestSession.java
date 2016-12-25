package lbn.quest.questData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

import lbn.quest.Quest;
import lbn.quest.quest.QuestType;

import com.google.common.collect.HashMultimap;

public class PlayerQuestSession {
//	HashMultimap<QuestType, String> doingQuest = HashMultimap.create();
	static HashMultimap<QuestType, Quest> doingQuest = HashMultimap.create();
	HashMap<String, Long> complateQuestLastComplateData = new HashMap<>();
	HashMap<String, Integer> complateQuestComplateCount = new HashMap<>();

	HashMap<String, Integer> complateQuestData = new HashMap<>();

	public boolean isDoing(Quest q) {
		return doingQuest.containsKey(q.getId());
	}

	public boolean isComplate(Quest q) {
		return complateQuestLastComplateData.containsKey(q.getId());
	}

	public Set<Quest> getDoingQuestListByType(QuestType type) {
		return doingQuest.get(type);
	}

	/**
	 * クエストのデータ値をセットする
	 * @param q
	 * @return
	 */
	public int getQuestData(Quest q) {
		if (complateQuestData.containsKey(q.getId())) {
			return complateQuestData.get(q.getId());
		}
		return 0;
	}

	public void setQuestData(Quest q, int data) {
		complateQuestData.put(q.getId(), data);
	}

	public long getComplateDate(Quest q) {
		if (complateQuestLastComplateData.containsKey(q.getId())) {
			return complateQuestLastComplateData.get(q.getId());
		}
		return 0;
	}

	public long getComplateCount(Quest q) {
		if (complateQuestComplateCount.containsKey(q.getId())) {
			return complateQuestComplateCount.get(q.getId());
		}
		return 0;
	}

	public void startQuest(Quest q) {
		doingQuest.put(q.getQuestType(), q);
	}

	public void removeQuest(Quest q) {
		doingQuest.remove(q.getQuestType(), q);
	}

	static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

	public void complateQuest(Quest q) {
		//完了時間をセット
		 Calendar cal1 = Calendar.getInstance(timeZone);
		 long timeInMillis = cal1.getTimeInMillis();
		complateQuestLastComplateData.put(q.getId(), timeInMillis);

		//完了時間をセット
		int complateCount = 1;
		if (complateQuestComplateCount.containsKey(q.getId())) {
			complateCount += complateQuestComplateCount.get(q.getId());
		}
		complateQuestComplateCount.put(q.getId(), complateCount);
	}
}
