package lbn.command.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TaskManager {
  HashMap<Location, CommandTask> taskMap = new HashMap<Location, CommandTask>();

  public void regist(CommandSender sender, long laterExecuteTick) {
    CommandTask commandTask = new CommandTask();

    Location senderLoc = null;
    if ((sender instanceof BlockCommandSender)) {
      commandTask.isCommandBlock = true;
      senderLoc = ((BlockCommandSender) sender).getBlock().getLocation();
    } else if (sender instanceof Player) {
      senderLoc = ((Player) sender).getLocation();
    }
    commandTask.loc = senderLoc;
    commandTask.endTime = laterExecuteTick * 50 + System.currentTimeMillis();
    taskMap.put(senderLoc, commandTask);
  }

  public void remove(Location locs) {
    taskMap.remove(locs);
  }

  public void showList(CommandSender send) {
    send.sendMessage(ChatColor.GREEN + "===== TASK STATUS =====");
    Iterator<Entry<Location, CommandTask>> iterator = taskMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Location, CommandTask> entry = iterator.next();
      CommandTask value = entry.getValue();
      if (value.remaindSecound() < 0) {
        iterator.remove();
        continue;
      }

      Location loc = value.loc;
      send.sendMessage(MessageFormat.format("Loc:{0} {1} {2},  残り時間:{3}s, コマブロ:{4}", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
          value.remaindSecound(), value.isCommandBlock));
    }
    send.sendMessage(ChatColor.GREEN + "===== TASK STATUS =====");
  }
}

class CommandTask {
  boolean isCommandBlock = false;

  Location loc = null;

  long endTime = 0;

  public int remaindSecound() {
    return (int) ((endTime - System.currentTimeMillis()) / 1000.0);
  }
}
