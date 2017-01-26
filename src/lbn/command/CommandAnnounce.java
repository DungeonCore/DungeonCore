package lbn.command;

import static java.lang.String.join;
import static lbn.api.other.Announcement.announce;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandAnnounce implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		announce(join(" ", args));
		return true;
	}

}
