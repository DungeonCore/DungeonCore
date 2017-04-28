package net.l_bulb.dungeoncore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.money.shop.Shop;

public class ShopCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    Player p = (Player) paramCommandSender;
    new Shop().openShop(p);

    return true;
  }

}