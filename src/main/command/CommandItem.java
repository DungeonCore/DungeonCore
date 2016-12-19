package main.command;

import main.lbn.SpletSheet.ItemSheetRunnable;
import main.lbn.SpletSheet.SpletSheetExecutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandItem implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		SpletSheetExecutor.onExecute(new ItemSheetRunnable(paramCommandSender));
		return true;
	}

}
