package net.l_bulb.dungeoncore.command;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.player.status.StatusViewerInventory;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandStatusView implements CommandExecutor {
  @SuppressWarnings("deprecation")
  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString, String[] params) {
    if (!(paramCommandSender instanceof Player)) {
      paramCommandSender.sendMessage("コンソールから実行できません。");
      return true;
    }

    Player sender = (Player) paramCommandSender;

    TheLowPlayer target = null;
    if (params.length == 0) {
      target = TheLowPlayerManager.getTheLowPlayer(sender);
    } else if (params.length >= 1) {
      String name = params[0];
      if (name.equals("show")) {
        target = TheLowPlayerManager.getTheLowPlayer(sender);
        return true;
      }

      OfflinePlayer player = Bukkit.getOfflinePlayer(name);
      target = TheLowPlayerManager.getTheLowPlayer(player);

      // もしロードされていなかったらロードする
      if (target == null) {
        try {
          TheLowPlayerManager.loadData(player);
        } catch (Exception e) {
          e.printStackTrace();
          sender.sendMessage("loadに失敗しました");
        }
      }
    }

    if (target != null) {
      openStatus(sender, target);
    } else {
      sender.sendMessage("現在、指定したPlayerを読み込んでいます。再度、同じコマンドを入力してください。もし何度やってもだめな場合はそのPlayerは存在しません。");
    }
    return true;
  }

  private void openStatus(Player sender, TheLowPlayer target) {
    Inventory statusView = StatusViewerInventory.getStatusView(target);
    sender.openInventory(statusView);
  }

}
