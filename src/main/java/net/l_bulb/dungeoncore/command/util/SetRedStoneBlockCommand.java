package net.l_bulb.dungeoncore.command.util;

import java.text.MessageFormat;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.util.BlockUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

public class SetRedStoneBlockCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String arg2,
      String[] arg3) {
    if (arg3.length != 3) { return false; }

    Location locationByCommandParam = JavaUtil
        .getValueOrDefaultWhenThrow(() -> MinecraftUtil.getLocationByCommandParam(sender, arg3[0], arg3[1], arg3[2], false), null);

    if (locationByCommandParam == null) {
      sender.sendMessage(MessageFormat.format("指定された座標が不正です：{0}, {1}, {2}", arg3[0], arg3[1], arg3[2]));
      return true;
    }

    BlockUtil.setRedstone(locationByCommandParam);
    return true;
  }

}
