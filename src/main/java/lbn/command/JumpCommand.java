package lbn.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;

public class JumpCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    if (paramArrayOfString.length == 0) { return false; }

    String pName = paramArrayOfString[0];
    Player playerExact = Bukkit.getPlayerExact(pName);
    if (playerExact == null) {
      paramCommandSender.sendMessage(pName + "というPlayerはいません");
      return true;
    }

    if (paramArrayOfString.length == 1) {
      playerExact.setVelocity(playerExact.getLocation().getDirection());
      LivingEntityUtil.setNoFallDamage(playerExact);
      return true;
    } else if (paramArrayOfString.length == 2) {
      double multiply = JavaUtil.getDouble(paramArrayOfString[1], 0);
      playerExact.setVelocity(playerExact.getLocation().getDirection().multiply(multiply));
      LivingEntityUtil.setNoFallDamage(playerExact);
      return true;
    } else if (paramArrayOfString.length == 4) {
      double x = JavaUtil.getDouble(paramArrayOfString[1], 0);
      double y = JavaUtil.getDouble(paramArrayOfString[2], 0);
      double z = JavaUtil.getDouble(paramArrayOfString[3], 0);
      playerExact.setVelocity(new Vector(x, y, z));
      LivingEntityUtil.setNoFallDamage(playerExact);
      return true;
    }
    return false;
  }

}
