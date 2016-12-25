package lbn.quest.quest;

import lbn.quest.Quest;

public enum QuestType {
	KILL_MOB_QUEST("アイテム採取"),
	PICK_ITEM_QUEST("モンスター討伐"),
	TOUCH_VILLAGER_QUEST("会話"),
	STRENGTH_ITEM_QUEST("アイテム強化"),
	UnknownQuest("未定義");

	String detail;
	private QuestType(String detail) {
		this.detail = detail;
	}

	public QuestType getInstance(String detail) {
		for (QuestType type : values()) {
			if (type.detail.equals(detail)) {
				return type;
			}
		}
		return UnknownQuest;
	}

	public Quest getQuestInstance(String id, String deta1, String deta2) {
		Quest q = null;
		switch (this) {
		case KILL_MOB_QUEST:
			q = KillMobQuest.getInstance(id, deta1, deta2);
		case PICK_ITEM_QUEST:
			q = PickItemQuest.getInstance(id, deta1, deta2);
		case TOUCH_VILLAGER_QUEST:
			q = TouchVillagerQuest.getInstance(id, deta1, deta2);
		default:
			break;
		}

		if (q == null) {
			q = new NullQuest(id);
		}
		return q;
	}
}
