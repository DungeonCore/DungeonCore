package lbn.quest.abstractQuest;

import java.util.Set;

import lbn.npc.NpcManager;
import lbn.quest.questData.PlayerQuestSession;
import lbn.util.QuestUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class TouchVillagerQuest extends AbstractQuest {
	static HashMultimap<String, TouchVillagerQuest> targetVillagerNameQuestList = HashMultimap.create();

	String villagerId;
	String[] talk;

	protected TouchVillagerQuest(String id, String villagerId, String[] talk) {
		super(id);
		this.villagerId = villagerId;
		this.talk = talk;
		targetVillagerNameQuestList.put(getTargetVillagerID(), this);
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

	public static Set<TouchVillagerQuest> getQuestByTargetVillager(Entity e) {
		String id = NpcManager.getId(e);
		if (id != null) {
			return targetVillagerNameQuestList.get(id);
		} else {
			return null;
		}
	}

	public void onTouchVillager(Player p, Entity entity, PlayerQuestSession session) {
		// NPCでないなら何もしない
		String id = NpcManager.getId(entity);
		if (id == null) {
			return;
		}
		if (id.equalsIgnoreCase(getTargetVillagerID())) {
			session.setQuestData(this, 1);

			// メッセージを出力
			QuestUtil.sendMessageByVillager(p, talk);
		}
	}

	public String getTargetVillagerID() {
		return villagerId;
	}

	@Override
	public String getCurrentInfo(Player p) {
		return "達成度(0/1)";
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
	public String getComplateCondition() {
		return villagerId + "と会って話をする";
	}
}
