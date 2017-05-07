package lbn.command.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lbn.util.BlockUtil;
import lbn.util.BlockUtil.BlockData;

public class ToggleSetBlockCommand implements CommandExecutor {
  @SuppressWarnings("deprecation")
  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
      String[] arg3) {

    if (arg3.length < 4) { return false; }

    BlockData blockData = BlockUtil.getBlockData(arg3[0], arg0);

    String commandList = StringUtils.join(Arrays.copyOfRange(arg3, 1, arg3.length), " ");
    String[] split = commandList.split("&");

    World w;
    if (arg0 instanceof Player) {
      w = ((Player) arg0).getWorld();
    } else if (arg0 instanceof BlockCommandSender) {
      w = ((BlockCommandSender) arg0).getBlock().getWorld();
    } else {
      arg0.sendMessage("その方法では実行できません。");
      return true;
    }

    for (String string : split) {
      try {
        String[] split2 = string.trim().split(" ");
        Location location = new Location(w, Double.parseDouble(split2[0]), Double.parseDouble(split2[1]), Double.parseDouble(split2[2]));
        Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
          block.setType(blockData.getM());
          block.setData(blockData.getB());
        } else {
          block.setType(Material.AIR);
          block.setData((byte) 0);
        }
      } catch (Exception e) {

      }
    }

    return true;
  }

}
