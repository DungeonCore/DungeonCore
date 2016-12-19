package lbn.command.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.common.other.RouteSearcher;

public class SearchPathCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		RouteSearcher instance = RouteSearcher.getInstance((Player)paramCommandSender);
		instance.startSearchingPath();
		paramCommandSender.sendMessage("足元にコマブロを設置しました。その中に座標が書かれているので「ctrl+A」で全部選択し、「strl+C」でコピーして使って下さい");
		return true;
	}

}
