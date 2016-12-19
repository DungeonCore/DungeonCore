package main.quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.quest.quest.NullQuest;

import org.bukkit.entity.Player;

public class QuestData {

	public static HashMap<Player, HashMap<Quest, Integer>> dataMap = new HashMap<>();


	public static int getData(Quest q, Player p) {
		initData(q, p);
		return dataMap.get(p).get(q);
	}

	protected static void initData(Quest q, Player p) {
		if (!dataMap.containsKey(p)) {
			dataMap.put(p, new HashMap<Quest, Integer>());
		}

		HashMap<Quest, Integer> hashMap = dataMap.get(p);
		if (!hashMap.containsKey(q)) {
			hashMap.put(q, 0);
		}
	}

	public static void setData(Quest q, Player p, int val) {
		initData(q, p);
		dataMap.get(p).put(q, val);
	}

	public static void remove(Quest q, Player p){
		if (!dataMap.containsKey(p)) {
			return;
		}

		HashMap<Quest, Integer> hashMap = dataMap.get(p);
		if (!hashMap.containsKey(q)) {
			return;
		}

		hashMap.remove(q);
	}

	public static void setJsonFormat(Player p, Map<String, Integer> map) {
		if (map == null) {
			return;
		}
		//データを削除する
		dataMap.remove(p);
		for (Entry<String, Integer> entry : map.entrySet()) {
			String id = entry.getKey();
			//idからクエストを取得
			Quest questById = QuestManager.getQuestById(id);
			if (questById == null) {
				questById = new NullQuest(id);
			}
			//データをセットする
			setData(questById, p, entry.getValue());
		}
	}

	public static Map<String, Integer> getJsonFormat(Player p) {
		HashMap<String, Integer> jsonFormat = new HashMap<String, Integer>();

		HashMap<Quest, Integer> hashMap = dataMap.get(p);

		if (hashMap != null) {
			for (Entry<Quest, Integer> entry : hashMap.entrySet()) {
				jsonFormat.put(entry.getKey().getId(), entry.getValue());
			}
		}

		return jsonFormat;
	}
}
