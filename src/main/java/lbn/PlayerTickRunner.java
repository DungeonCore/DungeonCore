package lbn;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.abstractQuest.ReachQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;

public class PlayerTickRunner {
  public static void execute() {
    new BukkitRunnable() {
      int tickCount = 0;
      ArrayList<Player> onlinePlayers;

      @Override
      public void run() {
        // Playerのリストを更新する
        if (tickCount == 0) {
          onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        }

        // 20人ごとにPlayerを操作する
        for (int i = 0; i < onlinePlayers.size(); i += 20) {
          Player player = onlinePlayers.get(i);

          checkQuest(player);
        }

        tickCount++;
        tickCount %= 20;
      }
    }.runTaskTimer(Main.plugin, 0, 1);
  }

  public static void checkQuest(Player p) {
    PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);

    for (ReachQuest quest : ReachQuest.fromChunk(p.getLocation())) {
      if (questSession.getProcessingStatus(quest) == QuestProcessingStatus.PROCESSING) {
        // クエスト完了処理
        questSession.setQuestData(quest, 1);
        quest.onSatisfyComplateCondtion(p);
      }
    }

  }
}
