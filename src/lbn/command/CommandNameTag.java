package lbn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNameTag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {

		}

		if (args.length == 1) {
			return true;
		}

		return true;
	}
/*
	private void argument(String args, CommandSender sender, String target) {
		switch (args) {
		case "set":
			set(sender, args, target);
			break;
		case "remove":
			remove(sender);
			break;
		case "list":
			list(sender);
			break;
		default:
			help(sender);
			return;
		}
	}

	public static void set(CommandSender sender, String args, String target) {
		switch (Tag.getTagType()) {
		case OWNER:
			break;
		case CO_OWNER:
			break;
		case DEVELOPER:
			break;
		case ADMIN:
			break;
		case SENIOR_BUILDER:
			break;
		case BUILDER:
			break;
		case JR_DEVELOPER:
			break;
		case JR_ADMIN:
			break;
		case JR_MODERATOR:
			break;
		case JR_BUILDER:
			break;
		case MANAGER:
			break;
		case MODERATOR:
			break;
		case HELPER:
			break;
		case TRAIAL:
			break;
		default:
			sender.sendMessage(ChatColor.RED + "入力された値、" + args + " はTagTypeに含まれません。");
			sender.sendMessage(ChatColor.DARK_GREEN + "/CustomNametag set [Tab]でTagTypeを取得出来ます。");
			break;
		}
	}
*/
}
