package net.l_bulb.dungeoncore.command.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class LoopCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
      String[] arg3) {
    if (arg3.length < 3) { return false; }

    String command = StringUtils.join(Arrays.copyOfRange(arg3, 2, arg3.length), " ");

    double secound = JavaUtil.getDouble(arg3[0], -1);

    if (secound == -1 || JavaUtil.getInt(arg3[1], -1) <= -1) {
      arg0.sendMessage("回数または間隔が不正です");
      return false;
    }

    if (secound == 0) {
      Bukkit.dispatchCommand(arg0, command);
    } else {
      new BukkitRunnable() {
        int count = JavaUtil.getInt(arg3[1], 1);

        @Override
        public void run() {
          if (count == 0) {
            cancel();
            return;
          }
          count--;
          Bukkit.dispatchCommand(arg0, command);
        }
      }.runTaskTimer(Main.plugin, 0, (long) (secound * 20));
    }

    return true;
  }

}
