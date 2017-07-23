package net.l_bulb.dungeoncore.command;

import net.l_bulb.dungeoncore.twitter.Twitter4JGateway;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import twitter4j.*;

public class BroadCastCommand implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(ChatColor.AQUA + "BroadCast: " + ChatColor.GOLD + "ゲーム内のチャットとTHELoWのTwitterAccountでツイートするコマンドです。");
      sender.sendMessage(ChatColor.GOLD + "利用例: /BroadCast こんにちは");
      return true;
    }

    if (!(args[1].length() <= 140)) {
      sender.sendMessage(ChatColor.AQUA + "BroadCast: " + ChatColor.RED + "140文字以内に収めて下さい");
      return true;
    }

    try {
      Twitter4JGateway.postTweet(args.toString());
    } catch (TwitterException ex) {
      sender.sendMessage(ChatColor.RED + "POSTING FAILD");
      ex.printStackTrace();
    }
    return true;
  }
}
