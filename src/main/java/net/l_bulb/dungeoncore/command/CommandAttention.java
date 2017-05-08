package net.l_bulb.dungeoncore.command;

import static java.lang.String.join;
import static net.l_bulb.dungeoncore.api.other.Announcement.attention;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandAttention implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length == 0) { return false; }
    attention(join(" ", args));
    return true;
  }

}
