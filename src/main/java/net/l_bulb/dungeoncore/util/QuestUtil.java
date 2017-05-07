package net.l_bulb.dungeoncore.util;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpc;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpcManager;
import net.l_bulb.dungeoncore.quest.QuestAnnouncement;

public class QuestUtil {
  /**
   * 村人から受けたクエストのクリア条件が満たされたときの処理
   * 
   * @param villagerName
   * @param p
   */
  public static void sendSatisfyComplateForVillager(String villagerId, Player p) {
    // TODO 音追加

    VillagerNpc npc = VillagerNpcManager.getVillagerNpcById(villagerId);
    // NPCが登録されていなければIDをそのまま返す
    if (npc == null) {
      p.sendMessage("クエストクリア!!!  " + villagerId + "のところに戻ろう！！");
      return;
    }

    Location location = npc.getLocation();

    String loc;
    if (location != null) {
      loc = MessageFormat.format("{0}, {1}, {2}", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    } else {
      loc = "現在は村人が存在しません";
    }

    ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    String command = MessageFormat.format(
        "tellraw {0} [\"\",{7}\"text\":\"{6}{1}\"},{7}\"text\":\"{2}\",\"hoverEvent\":{7}\"action\":\"show_text\",\"value\":\"Type : {3} ,Location : {4}\"}},{7}\"text\":\"{5}\"}]",
        p.getName(),
        "クエストクリア!!!  ",
        npc.getName(),
        npc.getEntityType(),
        loc,
        "のところに戻ろう",
        QuestAnnouncement.QUEST_INFO_PREFIX,
        "{");
    Bukkit.dispatchCommand(consoleSender, command);
  }

  static HashSet<UUID> inChatPlayer = new HashSet<>();

  public static void sendMessageByVillager(Player p, String[] text) {
    if (text == null || text.length == 0) { return; }

    // 現在、チャット中なら何もしない
    if (inChatPlayer.contains(p.getUniqueId())) { return; }

    p.sendMessage("");
    new LbnRunnable() {
      @Override
      public void run2() {
        // Playerがオフラインなら終了
        if (!p.isOnline()) {
          cancel();
          // チャット中から削除する
          inChatPlayer.remove(p.getUniqueId());
          return;
        }

        // もし現在のカウントがテキストの行以上ならストップ
        if (text.length <= getRunCount()) {
          cancel();
          // チャット中から削除する
          inChatPlayer.remove(p.getUniqueId());
          return;
        }
        // 逐次的にテキストを表示する
        p.sendMessage(ChatColor.GOLD + text[getRunCount()]);
        // チャット中ということを記録する
        inChatPlayer.add(p.getUniqueId());
      }
    }.runTaskTimer(15);
  }
}
