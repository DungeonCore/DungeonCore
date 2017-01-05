package lbn.command.util;

import lbn.common.text.SayText;
import lbn.dungeoncore.SpletSheet.SayTextSpletSheet;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SayTextCommand implements CommandExecutor{

	//CommandBlockListenerAbstract a(world)参照
	//saytext target textid
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player && arg3.length != 0 && arg3[0].equalsIgnoreCase("reload")) {
			reloadtext(arg0, arg3);
			return true;
		}

		if (arg3.length < 2) {
			return false;
		}

		Player playerExact = Bukkit.getPlayerExact(arg3[0]);
		if (playerExact == null) {
			arg0.sendMessage(arg3[0] + "というプレイヤーは存在しません。");
			return true;
		}

		String text = SayText.getSayText(arg3[1], playerExact);

		playerExact.sendMessage(text);
		return true;
	}

	public static void reloadAlltext() {
		SpletSheetExecutor.onExecute(new SayTextSpletSheet(Bukkit.getConsoleSender()));
	}

	private void reloadtext(CommandSender arg0, String[] arg3) {
		if (arg3[0].length() <= 1) {
			SpletSheetExecutor.onExecute(new SayTextSpletSheet(arg0));
		} else {
			SpletSheetExecutor.onExecute(new SayTextSpletSheet(arg0, arg3[1]));
		}
	}

}
