package lbn.chest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import lbn.util.DungeonLogger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class EachPlayerContentChest extends SpletSheetChest {

  Location chestLoc;

  public EachPlayerContentChest(Location chestLoc, Location contentLoc, int refuelTick, Location moveLoc,
      int minItemCount, int maxItemCount, int movetick, boolean random) {
    super(chestLoc, contentLoc, refuelTick, moveLoc, minItemCount, maxItemCount, movetick, random);
    this.chestLoc = chestLoc;
  }

  @Override
  public void open(Player p, Block block, PlayerInteractEvent e) {
    checkAllPlayerRefuelTime();

    Inventory inv = invList.get(p.getUniqueId());

    if (inv != null) {
      // refuelされていない時
      p.openInventory(inv);
    } else {
      // refuel後
      // １０秒後にテレポートする
      if (moveLoc != null) {
        teleportPlayer(p.getName());
      }
      // 新しいチェストを作成する
      Inventory newInventory = getNewInventory(p);
      // チェストを残しておく
      invList.put(p.getUniqueId(), newInventory);
      // refuel時間を残しておく
      refuelTime.put(p.getUniqueId(), System.currentTimeMillis() + (refuelTick * 50));
      p.openInventory(newInventory);
    }
  }

  Random rnd = new Random();

  /**
   * チェストの中身の入れ替えなどを行う
   */
  protected void checkAllPlayerRefuelTime() {
    long time = System.currentTimeMillis();
    // nullの可能性があるのでそのときは何もしない
    if (refuelTime == null || refuelTime.keySet() == null || invList == null) { return; }
    try {
      Iterator<Entry<UUID, Long>> iterator = refuelTime.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<UUID, Long> next = iterator.next();
        if (next.getValue() < time) {
          iterator.remove();
          invList.remove(next.getKey());
        }
      }
    } catch (Exception e) {
      DungeonLogger.error("EachPlayerContentsChest is error!!" + chestLoc);
    }
  }

  Map<UUID, Inventory> invList = new HashMap<UUID, Inventory>();

  Map<UUID, Long> refuelTime = new HashMap<UUID, Long>();

  HashSet<Player> set = new HashSet<Player>();

  protected boolean isFirestOpenWithPlayer(Player p) {
    return invList.containsKey(p.getUniqueId());
  }

  @Override
  public void setRefule(SpletSheetChest chest) {
    if (chest instanceof EachPlayerContentChest) {
      this.invList = ((EachPlayerContentChest) chest).invList;
      this.refuelTime = ((EachPlayerContentChest) chest).refuelTime;
    }
  }

}
