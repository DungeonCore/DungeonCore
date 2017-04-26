package lbn.chest;

import java.util.HashSet;

import lbn.dungeoncore.Main;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 全てのプレイヤーが同じ内容のチェストを参照する
 * 
 * @author kensuke
 *
 */
public class AllPlayerSameContentChest extends SpletSheetChest {

  public AllPlayerSameContentChest(Location chestLoc, Location contentLoc, int refuelTick, Location moveLoc,
      int minItemCount, int maxItemCount, int moveSec, boolean random) {
    super(chestLoc, contentLoc, refuelTick, moveLoc, minItemCount, maxItemCount, moveSec, random);
  }

  @Override
  public void open(Player p, Block block, PlayerInteractEvent e) {
    if (isFirestOpenWithPlayer(p)) {
      // 最初に開けてから１０秒後にテレポートする
      if (moveLoc != null) {
        teleportPlayer(p.getName());
      }
    }

    if (notOpen) {
      notOpen = false;
      new BukkitRunnable() {
        @Override
        public void run() {
          refuel(p);
        }
      }.runTaskLater(Main.plugin, refuelTick);
    }

    if (inv != null) {
      p.openInventory(inv);
    } else {
      Inventory newInventory = getNewInventory(p);
      if (newInventory == null) {
        p.sendMessage("Ouch!");
      } else {
        inv = newInventory;
        p.openInventory(newInventory);
      }
    }
  }

  boolean notOpen = true;

  Inventory inv;

  private void refuel(Player p) {
    set.clear();
    inv = null;
    notOpen = true;
  }

  HashSet<Player> set = new HashSet<Player>();

  protected boolean isFirestOpenWithPlayer(Player p) {
    if (set.contains(p)) { return false; }
    set.add(p);
    return true;
  }

  @Override
  public void setRefule(SpletSheetChest chest) {
    // if (chest instanceof AllPlayerSameContentChest) {
    // set = ((AllPlayerSameContentChest) chest).set;
    // inv = ((AllPlayerSameContentChest) chest).inv;
    // notOpen = ((AllPlayerSameContentChest) chest).notOpen;
    // }
  }

}
