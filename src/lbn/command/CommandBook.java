package lbn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.common.book.BookManager;
import lbn.item.ItemInterface;

public class CommandBook implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		if (paramArrayOfString.length < 2) {
			return false;
		}

		String opeName = paramArrayOfString[0];
		String bookid = paramArrayOfString[1];
		switch (opeName.toLowerCase()) {
		case "open":
			BookManager.opneBook((Player)paramCommandSender, bookid);
			break;
		case "item":
			ItemInterface item = BookManager.getItem(bookid);
			if (item != null) {
				((Player)paramCommandSender).getInventory().addItem(item.getItem());
			}
			break;
		default:
			break;
		}

		return false;
	}

}
