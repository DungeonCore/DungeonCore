package lbn.command.util;

import java.util.HashMap;

import lbn.dungeon.contents.item.key.KeyItemable;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CommandSpecialSign implements CommandExecutor {
  public static HashMap<Player, Location> signClickMap = new HashMap<Player, Location>();

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {

    if (!(paramCommandSender instanceof Player)) {
      paramCommandSender.sendMessage("playerのみ実行可能");
      return true;
    }

    Player p = (Player) paramCommandSender;

    ItemStack itemInHand = p.getItemInHand();
    if (ItemStackUtil.getName(itemInHand).isEmpty()) {
      p.sendMessage("消費するアイテムを持ってください。もしくはアイテム名が指定されていません。");
      return true;
    }

    Class<?> clazz = null;
    ItemInterface customItem;
    if (paramArrayOfString.length == 1 && paramArrayOfString[0].equalsIgnoreCase("get")) {
      customItem = ItemManager.getCustomItem(itemInHand);
      clazz = ItemInterface.class;
    } else {
      clazz = KeyItemable.class;
      customItem = ItemManager.getCustomItem(KeyItemable.class, itemInHand);
    }

    if (customItem == null) {
      p.sendMessage("現在持っているアイテムは登録されていません。");
      return true;
    }

    Location location = signClickMap.get(p);
    if (location == null) {
      p.sendMessage("先に空白の看板をクリエイティブで左クリックしてください");
      return false;
    }

    Block clickedBlock = location.getBlock();
    if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) {
      p.sendMessage("看板が存在しません");
      return false;
    }

    String line2 = null;

    String lastLine = null;
    if (clazz.equals(KeyItemable.class)) {
      lastLine = ((KeyItemable) customItem).getLastLine(p, paramArrayOfString);
      line2 = "IN HAND";
    } else if (paramArrayOfString[0].equalsIgnoreCase("get")) {
      line2 = "FOR GETTING";
    }

    if (line2 == null) { return false; }

    // 看板の内容を調べる
    Sign state = (Sign) clickedBlock.getState();
    state.setLine(0, ChatColor.GREEN + "CLICK HERE");
    state.setLine(1, ChatColor.GREEN + line2);
    state.setLine(2, ChatColor.stripColor(ItemStackUtil.getName(itemInHand)));
    state.setLine(3, lastLine);

    state.update();

    signClickMap.remove(p);
    return true;
  }

  public static boolean isWhiteSign(PlayerInteractEvent e) {
    Block clickedBlock = e.getClickedBlock();

    if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) { return false; }

    // 看板の内容を調べる
    Sign state = (Sign) clickedBlock.getState();
    String[] lines = state.getLines();
    int whiteCount = 0;
    for (String string : lines) {
      if (string == null || string.isEmpty()) {
        whiteCount++;
      }
    }
    return whiteCount == 4;
  }
}
