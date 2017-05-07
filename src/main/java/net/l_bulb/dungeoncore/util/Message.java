package net.l_bulb.dungeoncore.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.common.event.player.TheLowPlayerEvent;

public class Message {
  static String[] sign = { "{0}", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "{11}", "{12}", "{13}", "{14}" };

  @Deprecated
  public static String getMessage(Player p, String message, Object... arg) {
    return getMessage(message, arg);
  }

  public static String getMessage(String message, Object... arg) {
    if (arg.length == 0) { return message; }

    for (int i = 0; i < arg.length; i++) {
      message = message.replace(sign[i], arg[i].toString());
    }
    return message;
  }

  public static void sendMessage(Player p, String message, Object... arg) {
    if (p != null) {
      p.sendMessage(getMessage(p, message, arg));
    }
  }

  public static void sendMessage(TheLowPlayerEvent event, String message, Object... arg) {
    if (event == null) { return; }
    Player p = event.getPlayer();
    if (p != null) {
      p.sendMessage(getMessage(p, message, arg));
    }
  }

  public static void sendMessage(TheLowPlayer player, String message, Object... arg) {
    Player onlinePlayer = player.getOnlinePlayer();
    if (onlinePlayer != null) {
      sendMessage(player.getOnlinePlayer(), message, arg);
    }
  }

  public static void sendTellraw(Player p, String message, Object... arg) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getMessage(p, message, arg).replace("{player_name}", p.getName()));
  }

  public static String QUEST_START_MESSAGE = "tellraw {player_name} [\"\",{\"text\":\"" + getQuestName("{0}")
      + "を開始しました。(\"},{\"text\":\"/quest\",\"bold\":false,\"underlined\":false,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/quest\"}},{\"text\":\"でクエストを確認)\"}]";

  public static String QUEST_REMOVE_MESSAGE = getQuestName("{0}") + "を破棄しました。";

  public static String QUEST_OVER_COUNT = ChatColor.RED + "クエスト数が上限に達しました。どれかを破棄してください。";

  public static String QUEST_DOING_NOW = getQuestName("{0}") + "は実行中です";

  protected static String getQuestName(String name) {
    return ChatColor.WHITE + "クエスト[" + ChatColor.GOLD + name + ChatColor.WHITE + "] ";
  }

  public static String CANCEL_USE_ITEM_BY_LEVEL = ChatColor.RED + "このアイテムは{0}{1}以上のプレイヤーのみ使えます。";

  public static String MP_SHORTAGE = ChatColor.RED + "MPが足りません。";

  public static String ADD_DAMAGE_DISP = "ダメージ {1}{0}";

}
