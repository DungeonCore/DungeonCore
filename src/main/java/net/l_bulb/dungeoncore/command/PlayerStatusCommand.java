package net.l_bulb.dungeoncore.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.util.JavaUtil;

import net.md_5.bungee.api.ChatColor;

/**
 * <command> set type val target
 * <command> load target
 *
 */
public class PlayerStatusCommand implements CommandExecutor, TabCompleter {

  public static String[] oprateName = { "LOAD", "SET" };

  @SuppressWarnings("deprecation")
  @Override
  public boolean onCommand(CommandSender sender, Command paramCommand, String paramString, String[] params) {
    if (params.length < 2) { return false; }

    // データをLoadする
    if (params[0].equalsIgnoreCase("LOAD")) {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(params[1]);
      try {
        loadData(offlinePlayer, sender);
      } catch (Exception e) {
        sender.sendMessage("loadに失敗しました");
        return true;
      }
    } else if (params[0].equalsIgnoreCase("SET")) {
      // レベルをセットする
      LevelType type = null;
      OfflinePlayer target = null;
      int value = 0;
      switch (params.length) {
        case 0:
        case 1:
          type = LevelType.fromJpName(params[1]);
        case 2:
          value = JavaUtil.getInt(params[2], 0);
        case 3:
          target = Bukkit.getOfflinePlayer(params[3]);
          break;
        default:
          break;
      }

      if (type == null) {
        sender.sendMessage(ChatColor.RED + "type:" + params[1] + "が不正です");
        return true;
      }

      if (value == 0) {
        sender.sendMessage(ChatColor.GREEN + "レベルが0または不正な値だったので無視しました：" + params[2]);
        return true;
      }

      if (target == null) {
        target = (OfflinePlayer) sender;
      }

      TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(target);
      if (theLowPlayer == null) {
        sender.sendMessage(target.getName() + "のPlayerデータはロードされていません。先にロードしてください");
        return true;
      }

    }
    return false;
  }

  private void loadData(OfflinePlayer offlinePlayer, CommandSender paramCommandSender) throws Exception {
    if (offlinePlayer == null) {
      paramCommandSender.sendMessage("指定したPlayerのデータが存在しません。");
      return;
    }
    TheLowPlayerManager.loadData(offlinePlayer);
  }

  protected void setLevel(LevelType type, int value, TheLowPlayer target, Player sender) {
    target.setLevel(type, value);
    sender.sendMessage(type.getName() + "が" + target.getLevel(type) + "(" + target.getExp(type) + "xp)になりました。");
  }

  @Override
  public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
    if (paramArrayOfString.length == 1) {
      return Arrays.asList(oprateName);
    } else if (paramArrayOfString.length == 2 || !paramArrayOfString[0].equalsIgnoreCase("LOAD")) { return LevelType.getNames(); }
    return null;
  }

}
