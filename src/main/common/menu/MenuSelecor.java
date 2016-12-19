package main.common.menu;

import java.util.Arrays;
import java.util.HashMap;

import main.item.repair.newrepair.RepairUi;
import main.item.slot.table.SlotSetTableOperation;
import main.item.strength.StrengthTableOperation;
import main.util.ItemStackUtil;
import main.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MenuSelecor {
	static HashMap<String, MenuSelecor> selectorMap = new HashMap<String, MenuSelecor>();

	public static MenuSelecor getSelector(String title) {
		if (title.contains("--")) {
			return selectorMap.get(ChatColor.stripColor(title).trim());
		} else {
			return selectorMap.get("-- " + ChatColor.stripColor(title).trim() + " --");
		}
	}

	public static void regist(MenuSelecor menu) {
		selectorMap.put(ChatColor.stripColor("-- " + menu.title + " --"), menu);
	}
	public void regist() {
		regist(this);
	}

	public static boolean contains(String title) {
		return selectorMap.containsKey(ChatColor.stripColor(title).trim());
	}

	String title;
	Inventory createInventory;
	public MenuSelecor(String title) {
		this.title = title;
		createInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + "-- " + title + " --");
	}

	HashMap<ItemStack, SelectRunnable> runMap = new HashMap<ItemStack, SelectRunnable>();

	public MenuSelecor addMenu(ItemStack item, int index, SelectRunnable run) {
		createInventory.setItem(index, item);
		runMap.put(item, run);
		return this;
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

	static  {
		MenuSelecor menuSelecor = new MenuSelecor("blacksmith menu");
		//魔法石装着
		ItemStack itemStack = new ItemStack(Material.BEACON);
		ItemStackUtil.setDispName(itemStack, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("魔法石装着"));
		ItemStackUtil.setLore(itemStack, Arrays.asList(ChatColor.GREEN + Message.getMessage("武器に魔法石を装着します。"),
				ChatColor.GREEN + Message.getMessage("魔法石と武器を装着してください。"), "",
				ChatColor.GREEN + Message.getMessage("成功確率などのが、"),
				ChatColor.GREEN + Message.getMessage("赤いガラスの部分に表示されます。"))
				);
		menuSelecor.addMenu(itemStack, 15,
		new SelectRunnable() {
			@Override
			public void run(Player p) {
				SlotSetTableOperation.openSlotTable(p);
			}
		});

		//強化
		ItemStack itemStack2 = new ItemStack(Material.LAVA_BUCKET);
		ItemStackUtil.setDispName(itemStack2, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("強化"));
		ItemStackUtil.setLore(itemStack2, Arrays.asList(ChatColor.GREEN + Message.getMessage("アイテムを強化します。"),
				ChatColor.GREEN + Message.getMessage("強化するアイテムを置いてください。"), "",
				ChatColor.GREEN + Message.getMessage("成功確率・強化素材などが"),
				ChatColor.GREEN + Message.getMessage("赤いガラスの部分に表示されます。")
				));
		menuSelecor.addMenu(itemStack2, 13,
				new SelectRunnable() {
			@Override
			public void run(Player p) {
				StrengthTableOperation.openStrengthTable(p);
			}
		});

		ItemStack itemStack3 = new ItemStack(Material.ANVIL);
		ItemStackUtil.setDispName(itemStack3, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage("修理"));
		ItemStackUtil.setLore(itemStack3, Arrays.asList(Message.getMessage("アイテムの修理を行います。")));
		menuSelecor.addMenu(itemStack3, 11,
				new SelectRunnable() {
			@Override
			public void run(Player p) {
				RepairUi.onOpenUi(p);
			}
		});

		regist(menuSelecor);
	}
}
