package lbn.quest.abstractQuest;

import java.util.HashMap;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.quest.Quest;
import lbn.quest.questData.PlayerQuestSession;

import org.bukkit.entity.Player;

public class StrengthItemQuest extends AbstractQuest {

  static HashMap<String, StrengthItemQuest> questList = new HashMap<String, StrengthItemQuest>();

  public StrengthItemQuest(String id, String data1, String data2) {
    super(id);
    questList.put(getId(), this);
  }

  public static boolean isStrengthItemQuest(Quest q) {
    return questList.containsKey(q.getId());
  }

  public void onStrength(PlayerStrengthFinishEvent e, PlayerQuestSession session) {
    if (isQuestComplate(e)) {
      session.setQuestData(this, 1);
    }
  }

  protected boolean isQuestComplate(PlayerStrengthFinishEvent e) {
    return e.isSuccess();
  }

  @Override
  public String getCurrentInfo(Player p) {
    return "達成度(0/1)";
  }

  @Override
  public QuestType getQuestType() {
    return QuestType.STRENGTH_ITEM_QUEST;
  }

  @Override
  public boolean isComplate(int data) {
    return data == 1;
  }

  @Override
  public String getComplateCondition() {
    return "アイテムを一回強化する";
  }
}
