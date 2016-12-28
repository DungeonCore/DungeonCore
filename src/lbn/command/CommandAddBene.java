package lbn.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lbn.dungeon.contents.item.armor.Beneable;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.armoritem.BeneEffectManager;
import lbn.item.armoritem.BeneEffectType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class CommandAddBene implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		Player p = (Player) paramCommandSender;
		if (paramArrayOfString.length == 0) {
			return false;
		}

		ItemStack item = p.getItemInHand();
		ItemInterface customItem = ItemManager.getCustomItem(item);
		if (customItem == null) {
			return false;
		}

		if (!(customItem instanceof Beneable)) {
			p.sendMessage("ベネ装備を手に持ってください");
			return true;
		}

		ArrayList<BeneEffectType> beneList = new ArrayList<BeneEffectType>();
		for (String string : paramArrayOfString) {
			try {
				BeneEffectType valueOf = BeneEffectType.valueOf(string.toUpperCase());
				beneList.add(valueOf);
			} catch (Exception e) {
				p.sendMessage("ベネ名が違います:" + string);
				return true;
			}
		}

		BeneEffectManager.updateBeneLore(item, beneList);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		HashSet<String> itemNameList = getNameList();
		return (List<String>)StringUtil.copyPartialMatches(arg3[0], itemNameList, new ArrayList<String>(itemNameList.size()));
	}

	private HashSet<String> getNameList() {
		HashSet<String> hashSet = new HashSet<String>();
		for (BeneEffectType bene : BeneEffectType.values()) {
			hashSet.add(bene.toString());
		}
		return hashSet;
	}


}
