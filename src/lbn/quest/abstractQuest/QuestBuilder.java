package lbn.quest.abstractQuest;

import lbn.quest.Quest;
import lbn.util.JavaUtil;

public class QuestBuilder {
	Quest q;

	public QuestBuilder(String[] values) {
		try {
			String type = values[3];
			QuestType instance = QuestType.getInstance(type);
			q = instance.createQuestInstance(values[0], values[4], values[5]);

			if (!(q instanceof AbstractQuest)) {
				return;
			}

			AbstractQuest quest = (AbstractQuest) q;
			quest.name = values[1];
			if (values[2] != null) {
				quest.detail = values[2].split(",");
			} else {
				quest.detail = new String[0];
			}
			if (values[6] != null) {
				quest.talk1 = values[6].split(",");
			}
			if (values[7] != null) {
				quest.talk2 = values[7].split(",");
			}
			quest.isShowTitle = JavaUtil.getBoolean(values[8], true);
			quest.reworldItemId = values[9];
			quest.rewordMoney = JavaUtil.getInt(values[10], 0);
			quest.swordExe = JavaUtil.getInt(values[11], 0);
			quest.bowExe = JavaUtil.getInt(values[12], 0);
			quest.magicExe = JavaUtil.getInt(values[13], 0);
			if (values[14] != null) {
				quest.beforeQuestIds = values[14].split(",");
			}
			if (values[15] != null) {
				quest.autoExecuteNextQuestId = values[15].trim();
			}
			quest.isStartOverlap = JavaUtil.getBoolean(values[16], false);
			quest.canDestory = JavaUtil.getBoolean(values[17], true);
			quest.coolTimeSecound = JavaUtil.getInt(values[18], 0);
			quest.availableMainLevel = JavaUtil.getInt(values[19], 0);
			quest.startVillager = values[20];
			if (values[21] != null && !values[21].isEmpty()) {
				quest.endVillager = values[21];
			}
			quest.isProcessText = !"表示しない".equals(values[22]);

			quest.questBeforeItem = values[23];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Quest getQuest() {
		if (q.getName() == null || q.getName().isEmpty()) {
			return null;
		}
		if (q.getId() == null || q.getId().isEmpty()) {
			return null;
		}
		return q;
	}
}
