package lbn.command;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.StrengthOperator;
import lbn.item.strength.StrengthTableOperation;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StrengthItemCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {

		if (!(paramCommandSender instanceof Player)) {
			paramCommandSender.sendMessage("ゲーム内で実行してください。");
			return true;
		}

		Player p = (Player) paramCommandSender;

		if (paramArrayOfString.length == 1 && NumberUtils.isDigits(paramArrayOfString[0]) ) {
			ItemInterface item = ItemManager.getCustomItem(p.getItemInHand());
			if (item == null || !(item instanceof Strengthenable)) {
				paramCommandSender.sendMessage("今持っているアイテムは強化できません。");
				return true;
			}

			StrengthOperator.updateLore(p.getItemInHand(), Integer.parseInt(paramArrayOfString[0]));

			paramCommandSender.sendMessage(item.getItemName() +  "を" +  StrengthOperator.getLevel(p.getItemInHand()) + "に強化しました。");

			PlayerStrengthFinishEvent playerStrengthItemEvent = new PlayerStrengthFinishEvent(p, 100, Integer.parseInt(paramArrayOfString[0]), p.getItemInHand(), true);
			Bukkit.getServer().getPluginManager().callEvent(playerStrengthItemEvent);

			return true;

		} else if (paramArrayOfString.length == 1 && paramArrayOfString[0].equals("set") ) {
			StrengthTableOperation.openStrengthTable(p);
		}

		return false;
	}

}
