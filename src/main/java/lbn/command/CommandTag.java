package lbn.command;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.nametag.TagConfig;

public class CommandTag implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(
          DARK_GREEN + "■■================================================■■\n"
          + AQUA + "/tag list\n"
          + BOLD + "    タグの一覧を表示します\n"
          + ""
          + AQUA + "/tag settag <tag> <Player>\n"
          + BOLD + "    タグをセットします\n"
          + ""
          + AQUA + "/tag removetag <Player>\n"
          + BOLD + "    プレイヤーのタグを消します\n"
          + DARK_GREEN + "■■================================================■■");
      return true;
    }
    Player online = Bukkit.getPlayerExact(args[0]);
    if (online == null) {
      sender.sendMessage(RED + "Player" + args[0] + "はOfflineです。");
      return true;
    }
    TagConfig.setPlayer(args[1]);
    return true;
  }

}
