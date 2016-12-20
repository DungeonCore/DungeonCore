package lbn.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.item.GalionItem;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.util.Message;

public class MoneyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = (Player) arg0;
		if (arg3.length == 0) {
			int galion = GalionManager.getGalion(p);
			Message.sendMessage(p, ChatColor.GREEN + "You have {0} Galions", galion);
			return true;
		}

		if (!p.hasPermission("main.lbnDungeonUtil.command.admin.galions")) {
			return true;
		}

		if (arg3.length == 1) {
			Player playerExact = Bukkit.getPlayerExact(arg3[0]);
			if (playerExact != null) {
				int galion = GalionManager.getGalion(playerExact);
				arg0.sendMessage(playerExact.getDisplayName() + " have " + galion + " galions");
			} else {
				arg0.sendMessage(arg3[0] + "というプレイヤーは存在しません。");
			}
			return true;
		}

		if (arg3.length < 2) {
			return false;
		}
		int galions = 0;
		try {
			galions = Integer.parseInt(arg3[1]);
		}catch (NumberFormatException e) {
			p.sendMessage("金額が不正です");
			return false;
		}

		Player target = p;
		if (arg3.length == 3) {
			Player player = Bukkit.getPlayer(arg3[2]);
			if (player == null || !player.isOnline()) {
				p.sendMessage("Player名が不正です");
				return false;
			}
			p = player;
		}


		String operate = arg3[0];
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
