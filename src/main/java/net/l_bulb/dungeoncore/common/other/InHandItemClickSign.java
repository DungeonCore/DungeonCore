package net.l_bulb.dungeoncore.common.other;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.dungeon.contents.item.key.KeyItemable;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.GettingItemable;

public class InHandItemClickSign {
  boolean isSuccess = false;

  protected ItemInterface signItem;

  // private Location loc = null;

  Location signLoc;

  protected String[] lines;

  public InHandItemClickSign(PlayerInteractEvent e) {
    // 看板かどうか調べる
    Block clickedBlock = e.getClickedBlock();
    if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) { return; }
    signLoc = clickedBlock.getLocation();

    // 看板の内容を調べる
    Sign state = (Sign) clickedBlock.getState();
    lines = state.getLines();
    if (lines.length != 4) { return; }

    // 一行目
    if (!ChatColor.stripColor(lines[0]).equals(getLine1())) { return; }

    // 二行目
    if (!ChatColor.stripColor(lines[1]).equals(getLine2())) { return; }

    // ３行目はアイテム名かアイテムID
    ItemInterface item = ItemManager.getCustomItemById(lines[2]);
    if (item != null) {
      signItem = item;
      isSuccess = true;
      return;
    }
    ItemInterface item2 = ItemManager.getCustomItemByName(lines[2]);
    if (item2 != null) {
      signItem = item2;
      isSuccess = true;
      return;
    }
  }

  protected String getLine2() {
    return "IN HAND";
  }

  protected String getLine1() {
    return "CLICK HERE";
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void doClick(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    // 手に持っているアイテムのカスタムアイテムを取得
    KeyItemable itemInHand = ItemManager.getCustomItem(KeyItemable.class, e.getItem());
    if (itemInHand == null) {
      p.sendMessage(ChatColor.RED + "アイテム：" + signItem.getSimpleName() + "を持ってクリックしてください");
      return;
    }

    if (itemInHand.getId().equals(signItem.getId())) {
      itemInHand.onClick(e, lines, e.getItem());
    } else {
      p.sendMessage(ChatColor.RED + "アイテム：" + signItem.getSimpleName() + "を持ってクリックしてください");
    }
  }

  public static boolean onWriteSign(PlayerInteractEvent e) {
    KeyItemable itemInHand = ItemManager.getCustomItem(KeyItemable.class, e.getItem());
    if (itemInHand == null) { return false; }

    // 看板かどうか調べる
    Block clickedBlock = e.getClickedBlock();
    if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) { return false; }

    // 看板が空白かどうか調べる
    Sign state = (Sign) clickedBlock.getState();
    String[] lines = state.getLines();
    if (lines.length != 4) { return false; }
    for (String string : lines) {
      if (string != null && !string.isEmpty()) { return false; }
    }

    String line2 = null;

    String lastLine = null;
    if (itemInHand instanceof KeyItemable) {
      lastLine = itemInHand.getLastLine(e.getPlayer(), null);
      line2 = "IN HAND";
    } else if (itemInHand instanceof GettingItemable) {
      lastLine = ((GettingItemable) itemInHand).getLastLine(e.getPlayer(), null);
      line2 = "FOR GETTING";
    } else {
      return false;
    }

    if (lastLine == null) {
      lastLine = "";
    }

    Player player = e.getPlayer();
    if (player.getGameMode() != GameMode.CREATIVE) { return false; }

    if (!player.isSneaking()) {
      player.sendMessage(ChatColor.YELLOW + "シフトを押しながらクリックすることでこの看板をアイテム用に看板にできます。");
      return false;
    }

    // 看板の内容を調べる
    state.setLine(0, ChatColor.GREEN + "CLICK HERE");
    state.setLine(1, ChatColor.GREEN + line2);
    state.setLine(2, ChatColor.stripColor(itemInHand.getId()));
    state.setLine(3, lastLine);

    state.update();

    player.sendMessage(ChatColor.GREEN + "アイテム用の看板をセットしました。壊したい場合はWorldEditで壊してください。");

    return true;
  }
}
