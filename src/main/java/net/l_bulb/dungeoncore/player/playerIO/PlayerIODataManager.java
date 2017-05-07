package net.l_bulb.dungeoncore.player.playerIO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.Config;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.chest.wireless.WireLessChestManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.util.Message;

public class PlayerIODataManager {

  public static void load(Player p, String saveType) {
    try {
      TheLowPlayerManager.loadData(p);
      // チェストをロードする
      WireLessChestManager.getInstance().load(p);
    } catch (Exception e) {
      e.printStackTrace();
      kickPlayerByLoadData(p, e);
    }
  }

  public static void kickPlayerByLoadData(Player p, Throwable cause) {
    loadErrorList.add(p.getUniqueId());
    Message.sendMessage(p, ChatColor.RED + "PlayerDataのロードに失敗しました。\n"
        + ChatColor.RED + "管理者(twitter:{0})まで連絡ください。\n "
        + "ChatColor.RED + 10秒後に自動的にログアウトします。", Config.DEVELOPER_TWITTER_ID);
    new BukkitRunnable() {
      @Override
      public void run() {
        StringWriter writer = new StringWriter(1024);
        cause.printStackTrace(new PrintWriter(writer));

        p.kickPlayer("sorry, System cant load your game data. please tell developer (twitter:" + Config.DEVELOPER_TWITTER_ID + ")\n"
            + "申し訳ありません。あなたのゲームデータをロード出来ませんでした。 開発者(twitter: " + Config.DEVELOPER_TWITTER_ID + ")まで連絡ください。\n"
            + "技術情報: " + writer.toString());
      }
    }.runTaskLater(Main.plugin, 20 * 10);
  }

  public static HashSet<UUID> loadErrorList = new HashSet<>();

  public static void load(Player p) {
    String string = PlayerLastSaveType.getType(p);
    if (string == null) {
      load(p, "A");
    } else {
      load(p, string);
    }
  }

  public static void allLoad() {
    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
    for (Player player : onlinePlayers) {
      load(player);
    }
  }

  public static void allSave() {
    PlayerLastSaveType.save();
    for (Player player : Bukkit.getOnlinePlayers()) {
      save(player);
    }
  }

  public static void saveAsZip() {
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
    // String format = sdf.format(new Date());
    // String fileName = "backup_data_" + format + ".zip";
    // InOutputUtil.compressDirectory(new File(Main.dataFolder + File.separator + "debug" + File.separator + fileName).getAbsolutePath()
    // , Main.dataFolder + File.separator + "data");
  }

  public static void save(Player player) {
    TheLowPlayerManager.saveData(player);
  }
}
