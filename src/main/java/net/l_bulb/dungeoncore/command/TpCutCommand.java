package net.l_bulb.dungeoncore.command;

import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;

public class TpCutCommand implements CommandExecutor {
  static HashSet<String> hashSet = new HashSet<>();

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
      String[] arg3) {
    setTpCancel((Player) arg0);
    return true;
  }

  public static boolean isCancel(String name) {
    return hashSet.contains(name);
  }

  public static void setTpCancel(Player p) {
    if (hashSet.contains(p)) { return; }
    hashSet.add(p.getName());
    new BukkitRunnable() {
      @Override
      public void run() {
        hashSet.remove(p.getName());
      }
    }.runTaskLater(Main.plugin, 20 * 3);
  }

}
