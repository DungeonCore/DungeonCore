package lbn.quest.viewer;

import java.util.List;

import lbn.npc.CustomNpcInterface;
import lbn.quest.NpcQuestHolder;
import lbn.quest.Quest;

import org.bukkit.entity.Player;

public class QuestSelectorViewer {
  public static void openSelector(CustomNpcInterface villager, Player p) {
    // 実行可能クエスト
    List<Quest> canStartQuestList = NpcQuestHolder.getAvailableQuestList(villager, p);

    // 実行可能クエストがないなら何もしない
    if (canStartQuestList.isEmpty()) { return; }
    QuestMenuSelector questMenuSelector = new QuestMenuSelector(canStartQuestList);
    questMenuSelector.open(p);
  }
}
