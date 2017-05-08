package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.l_bulb.dungeoncore.npc.CustomNpcInterface;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpcManager;
import net.l_bulb.dungeoncore.quest.NpcQuestHolder;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestInventory;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.quest.QuestManager.QuestStartStatus;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CommandQuestOperate implements CommandExecutor, TabCompleter {
  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    boolean isSelectPlayer = false;

    Player p = (Player) arg0;
    if (arg3.length >= 3) {
      p = Bukkit.getPlayer(arg3[arg3.length - 1]);
      if (p == null) {
        p = (Player) arg0;
      } else {
        isSelectPlayer = true;
      }
    }

    if (arg3.length == 0) { return false; }
    if (arg3[0].equals("view")) {
      QuestInventory.openQuestViewer(p);
      return true;
    }

    if (arg3.length < 2) { return false; }

    // 一覧表示の場合はココで処理
    if (arg3[0].equals("npc")) {
      viewNpcQuestList(p, arg3[1]);
      return true;
    }

    String questName = StringUtils.join(Arrays.copyOfRange(arg3, 1, isSelectPlayer ? arg3.length - 1 : arg3.length), " ");

    Quest quest = null;
    // まずはIDから検索
    quest = QuestManager.getQuestById(questName);
    if (quest == null) {
      // 次に名前を検索
      quest = QuestManager.getQuestByName(questName);
    }
    if (quest == null) {
      arg0.sendMessage("クエストが存在しません:" + questName);
      return true;
    }

    if (arg3[0].equals("start")) {
      arg0.sendMessage("クエスト実行ステータス：" + QuestManager.getStartQuestStatus(quest, p).getCanntMessage());
      QuestManager.startQuest(quest, p, false, QuestManager.getStartQuestStatus(quest, p));
    } else if (arg3[0].equals("remove")) {
      QuestManager.removeQuest(quest, p);
    } else if (arg3[0].equals("complate")) {
      QuestManager.complateQuest(quest, p, false);
    } else {
      arg0.sendMessage("不明な命令です:" + arg3[0]);
      return true;
    }

    return true;
  }

  private void viewNpcQuestList(Player p, String string) {
    CustomNpcInterface villagerNpc = VillagerNpcManager.getVillagerNpcById(string);
    if (villagerNpc != null) {
      Set<Quest> questList = NpcQuestHolder.getQuestList(villagerNpc, p);
      for (Quest quest : questList) {
        QuestStartStatus startQuestStatus = QuestManager.getStartQuestStatus(quest, p);
        if (startQuestStatus == QuestStartStatus.CAN_START) {
          p.sendMessage(quest.getId() + " : " + quest.getName() + "(クエストを開始出来ます。)");
        } else {
          p.sendMessage(quest.getId() + " : " + quest.getName() + "(" + startQuestStatus.getCanntMessage() + ")");
        }
      }
    }
  }

  static HashSet<String> operateList = new HashSet<String>();
  static {
    operateList.add("start");
    operateList.add("remove");
    operateList.add("complate");
    operateList.add("view");
    operateList.add("npc");
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1,
      String arg2, String[] arg3) {
    if (arg3.length == 1) {
      return StringUtil.copyPartialMatches(arg3[0], operateList, new ArrayList<String>(operateList.size()));
    } else if (arg3.length == 2) {
      if (!arg3[0].equals("npc")) { return StringUtil.copyPartialMatches(arg3[1], QuestManager.getQuestId(), new ArrayList<String>(QuestManager
          .getQuestId().size())); }
    }
    return null;
  }

}
