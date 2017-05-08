package net.l_bulb.dungeoncore.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.l_bulb.dungeoncore.npc.CustomNpcInterface;

import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

/**
 * NPCとクエストの情報を管理するクラス
 */
public class NpcQuestHolder {
  static HashMultimap<String, Quest> villagerIDQuestMap = HashMultimap.create();

  public static void regist(Quest q) {
    villagerIDQuestMap.put(q.getStartVillagerId(), q);
  }

  public static List<Quest> getAvailableQuestList(CustomNpcInterface npc, Player p) {
    ArrayList<Quest> availableQuestList = new ArrayList<Quest>();

    Set<Quest> set = villagerIDQuestMap.get(npc.getId());
    for (Quest quest : set) {
      if (QuestManager.getStartQuestStatus(quest, p).canStart()) {
        availableQuestList.add(quest);
      }
    }
    return availableQuestList;
  }

  /**
   * 村人から全てのクエストを取得
   * 
   * @param npc
   * @param p
   * @return
   */
  public static Set<Quest> getQuestList(CustomNpcInterface npc, Player p) {
    return villagerIDQuestMap.get(npc.getId());
  }
}
