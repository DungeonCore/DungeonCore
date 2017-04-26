package lbn.player;

import java.util.HashMap;

import lbn.dungeoncore.Main;
import lbn.player.crafttable.CraftTableType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftTableViewManager {
  private static HashMap<Player, CraftTableType> craftTableViewMap = new HashMap<>();

  public static InventoryView openWorkbench(Player p, CraftTableType type) {
    Location loc = new Location(p.getWorld(), 0, 0, 0);
    if (loc.getBlock().getType() != Material.WORKBENCH) {
      loc.getBlock().setType(Material.WORKBENCH);
    }
    InventoryView openWorkbench = p.openWorkbench(loc, true);
    Inventory inv = openWorkbench.getTopInventory();

    craftTableViewMap.put(p, type);

    // 1tickごとに監視する
    new BukkitRunnable() {
      @Override
      public void run() {
        boolean contains = inv.getViewers().contains(p);
        if (!contains) {
          craftTableViewMap.remove(p);
          p.closeInventory();
          cancel();
        }

        if (!craftTableViewMap.containsKey(p)) {
          cancel();
        }
      }
    }.runTaskTimer(Main.plugin, 0, 1);

    return openWorkbench;
  }

  public static boolean isOpenCraftingTable(Player p, CraftTableType type) {
    return craftTableViewMap.containsKey(p) && craftTableViewMap.get(p) == type;
  }

  public static void forceCloseInventory(Player p) {
    p.closeInventory();
    craftTableViewMap.remove(p);
  }
}
