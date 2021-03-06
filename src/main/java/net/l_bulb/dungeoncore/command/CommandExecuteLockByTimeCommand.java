package net.l_bulb.dungeoncore.command;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.command.util.TaskManager;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

public class CommandExecuteLockByTimeCommand implements CommandExecutor {
  static TaskManager taskManager = new TaskManager();

  static HashMap<Location, Long> executeMap = new HashMap<>();

  @Override
  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    if (paramArrayOfString.length == 1 && paramArrayOfString[0].equals("list")) {
      taskManager.showList(paramCommandSender);
      return true;
    }

    // 実行者の座標を取得
    Location blockLoc = MinecraftUtil.getSenderLocation(paramCommandSender);
    if (blockLoc == null) { return false; }

    double second = 0;
    if (paramArrayOfString.length >= 2) {
      second = JavaUtil.getDouble(paramArrayOfString[0], -1);
    } else {
      return false;
    }

    if (second < 0) {
      paramCommandSender.sendMessage("実行時間間隔は0秒以上にしてください");
      return true;
    }

    if (!executeMap.containsKey(blockLoc)) {
      String command = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 1, paramArrayOfString.length), " ");
      Bukkit.dispatchCommand(paramCommandSender, command);
      executeMap.put(blockLoc, System.currentTimeMillis());

      taskManager.regist(paramCommandSender, (long) (second * 20));
      return true;
    }

    long beforeExecuteTime = executeMap.get(blockLoc);
    // もし(前実行した時間 + 指定した待ち時間) < (今の時間) ならコマンド実行
    if (beforeExecuteTime + second * 1000 < System.currentTimeMillis()) {
      String command = StringUtils.join(Arrays.copyOfRange(paramArrayOfString, 1, paramArrayOfString.length), " ");
      Bukkit.dispatchCommand(paramCommandSender, command);
      executeMap.put(blockLoc, System.currentTimeMillis());

      taskManager.regist(paramCommandSender, (long) (second * 20));
    }
    return true;
  }

}
