package lbn.command;

import lbn.util.JavaUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpOtherWorld implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command paramCommand, String paramString,
      String[] args) {
    if ((args.length < 1)) { return false; }
    Player player;
    if ((args.length == 1) || (args.length == 4)) {
      if ((sender instanceof Player)) {
        player = (Player) sender;
      } else {
        sender.sendMessage("Please provide a player!");
        return true;
      }
    } else {
      player = Bukkit.getPlayerExact(args[0]);
    }
    if (player == null) {
      sender.sendMessage("Player not found: " + args[0]);
      return true;
    }
    if (args.length < 3) {
      Player target = Bukkit.getPlayerExact(args[(args.length - 1)]);
      if (target == null) { return true; }
    } else if (player.getWorld() != null) {
      Location playerLocation = player.getLocation();
      double x = JavaUtil.getDouble(args[(args.length - 4)], -3.0000001E7D);
      double y = JavaUtil.getDouble(args[(args.length - 3)], -3.0000001E7D);
      double z = JavaUtil.getDouble(args[(args.length - 2)], -3.0000001E7D);
      if ((x == -3.0000001E7D) || (y == -3.0000001E7D)
          || (z == -3.0000001E7D)) {
        sender.sendMessage("Please provide a valid location!");
        return true;
      }

      World w = Bukkit.getWorld(args[(args.length - 1)]);
      if (w == null) {
        sender.sendMessage("ワールドが不正です。");
        return true;
      }
      playerLocation.setX(x);
      playerLocation.setY(y);
      playerLocation.setZ(z);
      playerLocation.setWorld(w);

      player.teleport(playerLocation);
    }
    return true;
  }

}
