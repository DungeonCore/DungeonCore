package lbn.quest.abstractQuest;

import lbn.quest.Quest;

public enum QuestType {
	PICK_ITEM_QUEST("アイテム採取"), KILL_MOB_QUEST("モンスター討伐"), TOUCH_VILLAGER_QUEST("会話"), STRENGTH_ITEM_QUEST(
			"アイテム強化"), REACH_QUEST("特定の場所に行く"), TAKE_ITEM_QUEST("特定のアイテムを特定のNPCに渡す"), UnknownQuest("未定義");

	String detail;

	private QuestType(String detail) {
		this.detail = detail;
	}

	public static QuestType getInstance(String detail) {
		for (QuestType type : values()) {
			if (type.detail.equals(detail)) {
				return type;
			}
		}
		return UnknownQuest;
	}

	public Quest createQuestInstance(String id, String deta1, String deta2) {
		Quest q = null;
		switch (this) {
		case KILL_MOB_QUEST:
			q = KillMobQuest.getInstance(id, deta1, deta2);
			break;
		case PICK_ITEM_QUEST:
			q = PickItemQuest.getInstance(id, deta1, deta2);
			break;
		case TOUCH_VILLAGER_QUEST:
			q = TouchVillagerQuest.getInstance(id, deta1, deta2);
			break;
		case TAKE_ITEM_QUEST:
			q = TakeItemQuest.getInstance(id, deta1, deta2);
			break;
		default:
			break;
		}

		if (q == null) {
			q = new NullQuest(id);
		}
		return q;
	}
}
