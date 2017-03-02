package lbn.command;

import java.util.ArrayList;
import java.util.List;

import lbn.item.strength.StrengthTableOperation;
import lbn.player.magicstoneOre.trade.MagicStoneTrade;
import lbn.player.reincarnation.ReincarnationFactor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class OpenCommand implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] params) {
		if (params.length == 0) {
			return false;
		}

		Player p = (Player) paramCommandSender;

		switch (params[0].toLowerCase()) {
		case "kajiya":
			StrengthTableOperation.openStrengthTable(p);
			break;
		case "reinc":
			ReincarnationFactor.openReincarnationInv(p);
			break;
		case "trade":
			MagicStoneTrade.open(p);
			break;
		default:
			break;
		}
		return true;
	}

	static ArrayList<String> arrayList = new ArrayList<String>();
	static {
		arrayList.add("kajiya");
		arrayList.add("reinc");
		arrayList.add("trade");
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], arrayList, new ArrayList<String>(arrayList.size()));
		}
		return null;
	}

}
