package lbn.command;

import static java.lang.String.*;
import static lbn.api.other.Announcement.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandAnnounce implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length == 0) { return false; }
    announce(join(" ", args));
    return true;
  }

}
