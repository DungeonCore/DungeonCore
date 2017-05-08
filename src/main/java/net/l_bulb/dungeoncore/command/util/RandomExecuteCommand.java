package net.l_bulb.dungeoncore.command.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.l_bulb.dungeoncore.util.JavaUtil;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RandomExecuteCommand implements CommandExecutor {
  Random rnd = new Random();

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
      String[] arg3) {
    if (arg3.length == 0) { return false; }

    int execuCount = 1;
    if (arg3[0].startsWith("-")) {
      execuCount = JavaUtil.getInt(arg3[0].replace("-", ""), 1);
      arg3 = Arrays.copyOfRange(arg3, 1, arg3.length);
    }

    String commandList = StringUtils.join(arg3, " ");
    List<String> asList = new ArrayList<String>(Arrays.asList(commandList.split("\\?")));

    if (asList.size() == 0) { return false; }

    for (int i = 0; i < Math.min(execuCount, asList.size()); i++) {
      String command = asList.remove(rnd.nextInt(asList.size()));
      Bukkit.dispatchCommand(arg0, command.trim());
    }
    return true;
  }

}
