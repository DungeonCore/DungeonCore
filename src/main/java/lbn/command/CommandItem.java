package lbn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lbn.dungeoncore.SpletSheet.ItemSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;

public class CommandItem implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    SpletSheetExecutor.onExecute(new ItemSheetRunnable(paramCommandSender));
    return true;
  }

}
