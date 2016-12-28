package lbn.common.menu;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MenuSelectorManager {
	static HashMap<String, MenuSelecor> selectorMap = new HashMap<String, MenuSelecor>();

	public static MenuSelecor getSelector(String title) {
		if (title.contains("--")) {
			return selectorMap.get(ChatColor.stripColor(title).trim());
		} else {
			return selectorMap.get("-- " + ChatColor.stripColor(title).trim() + " --");
		}
	}

	public static boolean contains(String title) {
		return selectorMap.containsKey(ChatColor.stripColor(title).trim());
	}

	public static void regist(MenuSelecor menu) {
		selectorMap.put(ChatColor.stripColor("-- " + menu.title + " --"), menu);
	}

	public static void open(Player p, String title) {
		MenuSelecor selector = getSelector(title);
		if (selector != null) {
			p.openInventory(selector.createInventory);
		}
	}

	public static void onSelect(InventoryInteractEvent e) {
		InventoryView view = e.getView();
		String title = view.getTitle();
		//もしセレクト画面でないなら何もしない
		if (title == null || !contains(title)) {
			return;
		}
		e.setCancelled(true);
		//クリックでないなら何もしない
		if (!(e instanceof InventoryClickEvent)) {
			return;
		}

		InventoryClickEvent event = (InventoryClickEvent) e;

		Player p = (Player) event.getWhoClicked();

		MenuSelecor selector = getSelector(title);

		ItemStack currentItem = event.getCurrentItem();
		if (selector.runMap.containsKey(currentItem)) {
			selector.runMap.get(currentItem).run(p);
		}
	}
}
