package net.l_bulb.dungeoncore.command;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.pic.AbstractPickaxe;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.item.system.strength.StrengthTables;

public class StrengthItemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {

    if (!(paramCommandSender instanceof Player)) {
      paramCommandSender.sendMessage("ゲーム内で実行してください。");
      return true;
    }

    Player p = (Player) paramCommandSender;

    if (paramArrayOfString.length == 1 && NumberUtils.isDigits(paramArrayOfString[0])) {
      ItemInterface item = ItemManager.getCustomItem(p.getItemInHand());
      if (item == null) {
        paramCommandSender.sendMessage("今持っているアイテムは強化できません。");
      } else if (item instanceof Strengthenable) {
        StrengthOperator.updateLore(p.getItemInHand(), Integer.parseInt(paramArrayOfString[0]));

        paramCommandSender.sendMessage(item.getItemName() + "を" + StrengthOperator.getLevel(p.getItemInHand()) + "に強化しました。");

        PlayerStrengthFinishEvent playerStrengthItemEvent = new PlayerStrengthFinishEvent(p, Integer.parseInt(paramArrayOfString[0]),
            p.getItemInHand(), true);
        Bukkit.getServer().getPluginManager().callEvent(playerStrengthItemEvent);
      } else if (item instanceof AbstractPickaxe) {
        ((AbstractPickaxe) item).updatePickExp(p.getItemInHand(), Short.parseShort(paramArrayOfString[0]));
        paramCommandSender.sendMessage(item.getItemName() + "のピッケルレベルを" + paramArrayOfString[0] + "に変更しました");
      } else {
        paramCommandSender.sendMessage("今持っているアイテムは強化できません。");
      }
      return true;
    } else if (paramArrayOfString.length == 1 && paramArrayOfString[0].equals("set")) {
      StrengthTables.openStrengthTable(p);
      return true;
    }

    return false;
  }

}
