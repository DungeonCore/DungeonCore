package lbn.command.util;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.util.BlockUtil;
import lbn.util.JavaUtil;

public class SetRedStoneBlockCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String arg2,
      String[] arg3) {
    if (arg3.length != 3) { return false; }

    double x = JavaUtil.getDouble(arg3[0], Double.NaN);
    double y = JavaUtil.getDouble(arg3[1], Double.NaN);
    double z = JavaUtil.getDouble(arg3[2], Double.NaN);

    if (x == Double.NaN || y == Double.NaN || z == Double.NaN) {
      sender.sendMessage("座標が不正です");
      return true;
    }

    Location senderLoc = null;
    if ((sender instanceof BlockCommandSender)) {
      senderLoc = ((BlockCommandSender) sender).getBlock().getLocation();
    } else if (sender instanceof Player) {
      senderLoc = ((Player) sender).getLocation();
    }
    if (senderLoc == null) { return false; }

    BlockUtil.setRedstone(new Location(senderLoc.getWorld(), x, y, z));

    return true;
  }

}
