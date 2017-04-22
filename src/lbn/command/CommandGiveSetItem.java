package lbn.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import lbn.item.setItem.SetItemInterface;
import lbn.item.setItem.SetItemManager;
import lbn.item.setItem.SetItemPartable;
import lbn.item.setItem.SetItemPartsType;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class CommandGiveSetItem implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		if (!(paramCommandSender instanceof Player)) {
			return false;
		}

		if (!((Player) paramCommandSender).isOp()) {
			return false;
		}

		if (paramArrayOfString == null || paramArrayOfString.length == 0) {
			paramCommandSender.sendMessage("item set name is null");
			return false;
		}

		String itemName = StringUtils.join(paramArrayOfString, " ").replace("_", " ");

		SetItemInterface setitem = SetItemManager.getSetItem(itemName.toUpperCase());
		if (setitem == null) {
			paramCommandSender.sendMessage("item set name が不正です");
			return true;
		}

		HashMap<SetItemPartsType, SetItemPartable> fullSetItem = setitem.getFullSetItem();
		for (SetItemPartable setItemParts : fullSetItem.values()) {
			((Player) paramCommandSender).getInventory().addItem(setItemParts.getItem());
		}

		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			HashSet<String> itemNameList = getSetItemNameList();
			return (List<String>) StringUtil.copyPartialMatches(arg3[0], itemNameList,
					new ArrayList<String>(itemNameList.size()));
		}
		return ImmutableList.of();
	}

	/**
	 * アイテム名一覧を取得
	 * 
	 * @return
	 */
	public HashSet<String> getSetItemNameList() {
		HashSet<String> nameList = new HashSet<String>();
		for (String name : SetItemManager.getSetItemNameList()) {
			nameList.add(name.replace(" ", "_"));
		}
		return nameList;
	}

}
