package net.l_bulb.dungeoncore.command.util;

import java.lang.ref.SoftReference;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;

public class SequenceCommand implements CommandExecutor, UsageCommandable {
  @Override
  public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("実行間隔の秒数が必要です。例) sequencecommand 3 say 123 & say abc ");
      return false;
    }

    double second;
    try {
      second = Double.parseDouble(args[0]);
    } catch (NumberFormatException e) {
      sender.sendMessage(args[0] + " is not a valid number!!");
      return false;
    }

    String commandList = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
    final String[] split = commandList.split("&");

    new BukkitRunnable() {
      int i = 0;

      @Override
      public void run() {
        Bukkit.dispatchCommand(sender, split[i].trim());
        i++;
        if (split.length <= i) {
          cancel();
        }
      }
    }.runTaskTimer(Main.plugin, 0, (long) (second * 20));

    return true;
  }

  private static SoftReference<String> usageCache;

  @Override
  public String getUsage() {
    String message;

    if (usageCache != null) {
      message = usageCache.get();
      if (message != null) { return message; }
    }

    message = "/sequencecommand second command1 & command2 & ... "
        + ChatColor.GREEN + "\n '/sequencecommand 1 say 123 & say 456' & say 789' "
        + ChatColor.GRAY + "\n ---- 1秒間隔で'/say 123'と'/say 456'と'/say 789'を実行します。"
        + ChatColor.GREEN + "\n '/sequencecommand 0 say こんにちは & setblock 60 60 60 1' "
        + ChatColor.GRAY + "\n ---- 連続で'/say こんにちわ'と'/setblock 60 60 60 1'を実行します。"
        + ChatColor.GREEN + "\n '/sequencecommand 0 say こんにちは & delaycommand 5 say こんばんわ' "
        + ChatColor.GRAY + "\n ---- 連続で'/say こんにちわ'と'/delaycommand 5 say こんばんわ'を実行します。"
        + ChatColor.GRAY + "\n ---- つまり'/say こんにちわ'を実行した5秒後、'/say こんばんわ'を実行します。"
        + ChatColor.GREEN + "\n 応用すればコマンド実行後に穴を作り、10秒後に塞ぐというようなことも出来ます。";

    usageCache = new SoftReference<>(message);
    return message;
  }

  @Override
  public String getDescription() {
    return "指定した時間間隔で連続してコマンドを実行します。";
  }

}