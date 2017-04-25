package lbn.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.nametag.TagConfig;

public class CommandTag implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_GREEN + "■■================================================■■");
			sender.sendMessage(ChatColor.AQUA + "/tag list");
			sender.sendMessage(ChatColor.BOLD + "タグの一覧を表示します");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.AQUA + "/tag settag <tag> <Player>" );
			sender.sendMessage(ChatColor.BOLD + "↳タグをセットします");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.AQUA + "/tag removetag <Player>");
			sender.sendMessage(ChatColor.BOLD + "↳プレイヤーのタグを消します");
			sender.sendMessage(ChatColor.DARK_GREEN + "■■================================================■■");
			return true;
		}
		TagConfig.setNewTag(commandLabel);
		Player online = Bukkit.getPlayerExact(args[0]);
		if (online == null) {
			sender.sendMessage(ChatColor.RED + "Player" + args[0] + "はOfflineです。");
			return true;
		}
		TagConfig.setPlayer(args[1]);
		return true;
	}

}
