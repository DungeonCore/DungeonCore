package lbn.command;

import java.util.ArrayList;
import java.util.List;

import lbn.common.book.BookManager;
import lbn.item.ItemInterface;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class CommandBook implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {

		if (paramArrayOfString.length == 1 && paramArrayOfString[0].equals("list")) {
			paramCommandSender.sendMessage(BookManager.getNames().toString());
			return true;
		}

		if (paramArrayOfString.length < 2) {
			return false;
		}

		String opeName = paramArrayOfString[0];
		String bookid = paramArrayOfString[1];
		Player player = null;

		if (paramArrayOfString.length >= 3) {
			player = Bukkit.getPlayerExact(paramArrayOfString[2]);

			if (player == null) {
				paramCommandSender.sendMessage("Player:" + paramArrayOfString[2] + "が存在しません。");
				return true;
			}
		}

		if (paramCommandSender instanceof Player && player == null) {
			player = (Player)paramCommandSender;
		}

		if (player == null) {
			paramCommandSender.sendMessage("Playerを指定してください");
			return false;
		}

		ItemInterface item2 = BookManager.getItem(bookid);
		if (item2 == null) {
			paramCommandSender.sendMessage(ChatColor.RED + "本が見つかりません。 id" + bookid);
			return true;
		}

		switch (opeName.toLowerCase()) {
		case "open":
			BookManager.opneBook(player, bookid);
			return true;
		case "item":
			ItemInterface item = BookManager.getItem(bookid);
			if (item != null) {
				player.getInventory().addItem(item.getItem());
			}
			return true;
		case "reload":
			BookManager.reloadSpletSheet(paramCommandSender);
			return true;
		default:
			return false;
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 2) {
			return (List<String>) StringUtil.copyPartialMatches(arg3[1], BookManager.getNames(),
					new ArrayList<String>(BookManager.getNames().size()));
		}
		return ImmutableList.of();
	}
}
