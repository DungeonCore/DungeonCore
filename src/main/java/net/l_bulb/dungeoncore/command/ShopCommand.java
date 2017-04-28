package net.l_bulb.dungeoncore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.money.shop.Shop;

public final class ShopCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player p = (Player) sender;
    new Shop().openShop(p);
    return true;
  }

}
