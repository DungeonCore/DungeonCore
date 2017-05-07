package lbn.common.menu;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MenuSelectorManager {
  static HashMap<String, MenuSelectorInterface> selectorMap = new HashMap<String, MenuSelectorInterface>();

  public static MenuSelectorInterface getSelector(String title) {
    return selectorMap.get(ChatColor.stripColor(title).trim());
  }

  public static boolean contains(String title) {
    return selectorMap.containsKey(ChatColor.stripColor(title).trim());
  }

  public static void regist(MenuSelectorInterface menu) {
    selectorMap.put(ChatColor.stripColor(menu.getTitle()), menu);
  }

  public static void open(Player p, String title) {
    MenuSelectorInterface selector = getSelector(title);
    if (selector != null) {
      selector.open(p);
    } else {
      new RuntimeException("menu not found:" + title).printStackTrace();
    }
  }

  public static void onSelect(InventoryInteractEvent e) {
    InventoryView view = e.getView();
    String title = view.getTitle();
    // もしセレクト画面でないなら何もしない
    if (title == null || !contains(title)) { return; }
    e.setCancelled(true);
    // クリックでないなら何もしない
    if (e instanceof InventoryClickEvent) {
      InventoryClickEvent event = (InventoryClickEvent) e;

      // クリックしたのが上のインベントリでないなら無視
      if (event.getClickedInventory() != view.getTopInventory()) { return; }

      Player p = (Player) event.getWhoClicked();

      MenuSelectorInterface selector = getSelector(title);

      ItemStack currentItem = event.getCurrentItem();
      selector.onSelectItem(p, currentItem, event);
    }

  }
}
