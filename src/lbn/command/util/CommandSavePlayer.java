package lbn.command.util;

import lbn.player.playerIO.PlayerIODataManager;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSavePlayer implements CommandExecutor{

	//command type [load or save] player
	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {

		Player target = null;
		if (paramCommandSender instanceof Player) {
			target = (Player) paramCommandSender;
		}

		if (paramArrayOfString.length == 3) {
			Player playerExact = Bukkit.getPlayerExact(paramArrayOfString[2]);
			if (playerExact == null) {
				paramCommandSender.sendMessage("not found player:" + paramArrayOfString[2]);
				return true;
			}
			target = playerExact;
		}

		if (target == null) {
			paramCommandSender.sendMessage("no target player");
			return false;
		}

		if (paramArrayOfString.length <= 2) {
			return false;
		}

		String type = paramArrayOfString[0];

		String operate = paramArrayOfString[1];

		if  (operate.equalsIgnoreCase("save")) {
			PlayerIODataManager.save(target);
			Message.sendMessage(target, "データをセーブしました。");
		} else if (operate.equalsIgnoreCase("load")) {
			Message.sendMessage(target, "データをロードしました。");
			//まずセーブする
			PlayerIODataManager.save(target);
			PlayerIODataManager.load(target, type);
		} else {
			return false;
		}

		return true;
	}

}
