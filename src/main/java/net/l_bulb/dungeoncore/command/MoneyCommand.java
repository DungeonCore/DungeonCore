package net.l_bulb.dungeoncore.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.item.customItem.other.GalionItem;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.util.Message;

public class MoneyCommand implements CommandExecutor {

  @SuppressWarnings("deprecation")
  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length == 0 || args.length == 1) { return showMoney(sender, args); }

    Player p = (Player) sender;

    if (!p.hasPermission("main.lbnDungeonUtil.command.admin.galions")) { return true; }

    if (args.length < 2) { return false; }
    int galions = 0;
    try {
      galions = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      p.sendMessage("金額が不正です");
      return false;
    }

    TheLowPlayer theLowPlayer = null;
    if (args.length == 3) {
      theLowPlayer = TheLowPlayerManager.getTheLowPlayer(Bukkit.getOfflinePlayer(args[2]));
    } else {
      theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player) sender);
    }

    if (theLowPlayer == null) {
      sender.sendMessage(ChatColor.RED + "指定されたPlayerのデータをロードするので時間を開けて再度同じコマンドを実行してください。もしこのメッセージが何度も表示される場合は指定したPlayerのデータは存在しません");
      return true;
    }

    String operate = args[0];
    if (operate.equalsIgnoreCase("set")) {
      theLowPlayer.setGalions(galions, GalionEditReason.command);
    } else if (operate.equalsIgnoreCase("add")) {
      theLowPlayer.addGalions(galions, GalionEditReason.command);
    } else if (operate.equalsIgnoreCase("item")) {
      GalionItem galionItem = GalionItem.getInstance(galions);
      p.getInventory().addItem(galionItem.getItem());
    } else {
      p.sendMessage("不正なコマンドです");
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  public boolean showMoney(CommandSender sender, String[] args) {
    Player p = (Player) sender;
    TheLowPlayer theLowPlayer = null;
    if (args.length == 0) {
      theLowPlayer = TheLowPlayerManager.getTheLowPlayer((Player) sender);
    } else {
      if (!p.hasPermission("main.lbnDungeonUtil.command.admin.galions")) {
        theLowPlayer = TheLowPlayerManager.getTheLowPlayer(Bukkit.getOfflinePlayer(args[1]));
        return false;
      }
    }

    if (theLowPlayer == null) {
      sender.sendMessage(ChatColor.RED + "指定されたPlayerのデータをロードするので時間を開けて再度同じコマンドを実行してください。もしこのメッセージが何度も表示される場合は指定したPlayerのデータは存在しません");
      return true;
    }

    int galions = theLowPlayer.getGalions();
    if (p.getUniqueId().equals(theLowPlayer.getUUID())) {
      Message.sendMessage(p, ChatColor.GREEN + "You have {0} Galions", galions);
    } else {
      Message.sendMessage(p, ChatColor.GREEN + theLowPlayer.getName() + " have {0} Galions", galions);
    }

    return true;
  }

}
