package lbn.command;

import lbn.item.GalionItem;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (args.length == 0) {
			int galion = GalionManager.getGalion(p);
			Message.sendMessage(p, ChatColor.GREEN + "You have {0} Galions", galion);
			return true;
		}

		if (!p.hasPermission("main.lbnDungeonUtil.command.admin.galions")) {
			return true;
		}

		if (args.length == 1) {
			Player target = Bukkit.getPlayerExact(args[0]);
			if (target != null) {
				int galion = GalionManager.getGalion(target);
				sender.sendMessage(target.getDisplayName() + " have " + galion + " galions");
			} else {
				sender.sendMessage(args[0] + "というプレイヤーは存在しません。");
			}
			return true;
		}

		if (args.length < 2) {
			return false;
		}
		int galions = 0;
		try {
			galions = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			p.sendMessage("金額が不正です");
			return false;
		}

		Player target = p;
		if (args.length == 3) {
			Player player = Bukkit.getPlayer(args[2]);
			if (player == null || !player.isOnline()) {
				p.sendMessage("Player名が不正です");
				return false;
			}
			p = player;
		}

		String operate = args[0];
		if (operate.equalsIgnoreCase("set")) {
			GalionManager.setGalion(target, galions, GalionEditReason.command);
		} else if (operate.equalsIgnoreCase("add")) {
			GalionManager.addGalion(target, galions, GalionEditReason.command);
		} else if (operate.equalsIgnoreCase("item")) {
			GalionItem galionItem = GalionItem.getInstance(galions);
			p.getInventory().addItem(galionItem.getItem());
		} else {
			p.sendMessage("不正なコマンドです");
		}
		return true;
	}

}
