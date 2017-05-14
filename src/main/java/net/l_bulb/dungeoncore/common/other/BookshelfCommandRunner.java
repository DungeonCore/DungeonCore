package net.l_bulb.dungeoncore.common.other;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;

public class BookshelfCommandRunner {
  boolean canDo = false;

  String commad = null;

  Location bedRockLoc;

  Player clickPlayer;

  public BookshelfCommandRunner(PlayerInteractEvent e) {
    if (e.getAction() == Action.PHYSICAL || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) { return; }

    Block clickedBlock = e.getClickedBlock();
    if (clickedBlock == null || clickedBlock.getType() == Material.AIR) { return; }

    if (clickedBlock.getType() != Material.BOOKSHELF) { return; }

    CommandBlock state = null;

    Location location = clickedBlock.getLocation();
    if (location.add(1, 0, 0).getBlock().getType() == Material.COMMAND) {
      state = (CommandBlock) location.getBlock().getState();
    } else if (location.add(-1, 0, 1).getBlock().getType() == Material.COMMAND) {
      state = (CommandBlock) location.getBlock().getState();
    } else if (location.add(-1, 0, -1).getBlock().getType() == Material.COMMAND) {
      state = (CommandBlock) location.getBlock().getState();
    } else if (location.add(1, 0, -1).getBlock().getType() == Material.COMMAND) {
      state = (CommandBlock) location.getBlock().getState();
    }

    if (state == null) { return; }
    Block block = location.add(0, -1, 0).getBlock();
    if (block.getType() != Material.BEDROCK) { return; }
    bedRockLoc = block.getLocation();

    Block underBlock = bedRockLoc.clone().add(0, -1, 0).getBlock();
    if (underBlock.isEmpty() || underBlock.isLiquid()) {
      if (e.getPlayer().isOp()) {
        e.getPlayer().sendMessage(ChatColor.RED + "岩盤の下にブロックが必要です。");
      }
      return;
    }

    commad = state.getCommand();
    // コマンドがない場合は無視
    if (commad == null) { return; }

    clickPlayer = e.getPlayer();

    canDo = true;
  }

  public boolean canDo() {
    return canDo;
  }

  public void doCommand() {
    // open bookコマンド
    commad = commad.replaceFirst("/", "").toLowerCase();
    if (commad.toLowerCase().startsWith("book open")) {
      // 本棚をクリックしたPlayerを実行者にする
      String[] split = commad.split(" ");
      if (split.length == 4 && split[3].contains("@p")) {
        split[3] = clickPlayer.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.join(split, " "));
        return;
      }
    }

    bedRockLoc.getBlock().setType(Material.REDSTONE_BLOCK);

    new BukkitRunnable() {
      @Override
      public void run() {
        bedRockLoc.getBlock().setType(Material.BEDROCK);
      }
    }.runTaskLater(Main.plugin, 2);
  }
}
