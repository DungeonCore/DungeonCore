package lbn.quest.quest;

import lbn.quest.Quest;
import lbn.util.JavaUtil;

public class QuestBuilder {
	Quest q;

	public QuestBuilder(String[] values) {
		String type = values[3];
		QuestType instance = QuestType.getInstance(type);
		q = instance.createQuestInstance(values[0], values[4], values[5]);

		if (!(q instanceof AbstractQuest)) {
			return;
		}

		AbstractQuest quest = (AbstractQuest)q;
		quest.name = values[1];
		quest.detail = values[2];
		quest.talk1 = values[6].split(",");
		quest.talk2 = values[7].split(",");
		quest.isShowTitle = JavaUtil.getBoolean(values[8], true);
		quest.reworldItemId = values[9];
		quest.rewordMoney = JavaUtil.getInt(values[10], 0);
		quest.swordExe = JavaUtil.getInt(values[11], 0);
		quest.bowExe = JavaUtil.getInt(values[12], 0);
		quest.magicExe = JavaUtil.getInt(values[13], 0);
		quest.beforeQuestIds = values[14].split(",");
		quest.autoExecuteNextQuestId = values[15].trim();
		quest.isStartOverlap = JavaUtil.getBoolean(values[16], false);
		quest.canDestory = JavaUtil.getBoolean(values[17], true);
		quest.coolTimeSecound = JavaUtil.getInt(values[18], 0);
		quest.availableMainLevel = JavaUtil.getInt(values[19], 0);
	}

	public Quest getQuest() {
		return q;
	}
}
