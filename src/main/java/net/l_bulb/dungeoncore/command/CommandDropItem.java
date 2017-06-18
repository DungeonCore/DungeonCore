package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

import com.google.common.collect.ImmutableList;

public class CommandDropItem implements CommandExecutor, TabCompleter {

  /**
   * <command> itemid x y z
   */
  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length < 4) { return false; }

    Location loc = MinecraftUtil.getLocationByCommandParam(arg0, arg3[1], arg3[2], arg3[3], false);
    // アイテムを取得
    ItemInterface item = ItemManager.getCustomItemById(arg3[0]);
    if (item == null) {
      arg0.sendMessage("アイテムIDが不正です：" + arg3[0]);
      return true;
    }
    loc.getWorld().dropItem(loc, item.getItem());

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length == 1) { return StringUtil.copyPartialMatches(arg3[0], ItemManager.getAllItemID(),
        new ArrayList<String>(ItemManager.getAllItemID().size())); }
    return ImmutableList.of();
  }
}
