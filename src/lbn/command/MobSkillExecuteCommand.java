package lbn.command;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;

public class MobSkillExecuteCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		System.out.println(paramCommandSender + "@" + Arrays.toString(paramArrayOfString));
		System.out.println(((ProxiedCommandSender)paramCommandSender).getCaller());
		return true;
	}

}
