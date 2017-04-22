package lbn.command.util;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RandomExecuteCommand implements CommandExecutor {
	Random rnd = new Random();

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 0) {
			return false;
		}

		String commandList = StringUtils.join(Arrays.copyOfRange(arg3, 0, arg3.length), " ");
		String[] split = commandList.split("\\?");

		if (split.length == 0) {
			return false;
		}

		String command = split[rnd.nextInt(split.length)];
		Bukkit.dispatchCommand(arg0, command.trim());
		return true;
	}

}
