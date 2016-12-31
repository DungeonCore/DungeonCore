package lbn.command;

import static java.lang.String.*;
import static lbn.Announcement.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandAttention implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		attention(join(" ", args));
		return true;
	}

}
