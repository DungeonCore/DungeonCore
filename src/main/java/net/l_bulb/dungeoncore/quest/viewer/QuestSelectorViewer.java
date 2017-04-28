package net.l_bulb.dungeoncore.quest.viewer;

import java.util.List;

import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.npc.CustomNpcInterface;
import net.l_bulb.dungeoncore.quest.NpcQuestHolder;
import net.l_bulb.dungeoncore.quest.Quest;

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
