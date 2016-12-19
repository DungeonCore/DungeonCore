package lbn.money;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import lbn.money.buyer.Buyer;
import lbn.money.shop.CustomShop;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

public class BuyerShopSelector {
	private static final String SHOP_MENU = "- Shop - ";

	public static void onOpen(Player p, String villagerName) {
		Inventory createInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + SHOP_MENU + villagerName);
		createInventory.setItem(11, getShopButton(p));
		createInventory.setItem(15, getBuyerButton(p));
		p.openInventory(createInventory);
	}

	public static void onSelect(InventoryInteractEvent e) {
		InventoryView view = e.getView();
		String title = view.getTitle();
		//もしセレクト画面でないなら何もしない
		if (title == null || !title.contains(SHOP_MENU)) {
			return;
		}
		e.setCancelled(true);
		//クリックでないなら何もしない
		if (!(e instanceof InventoryClickEvent)) {
			return;
		}

		String villagerName = title.replace(SHOP_MENU, "");

		InventoryClickEvent event = (InventoryClickEvent) e;

		Player p = (Player) event.getWhoClicked();

		ItemStack currentItem = event.getCurrentItem();
		if (currentItem != null && currentItem.equals(getBuyerButton(p))) {
			Buyer.onOpen(p);
		} else if (currentItem != null && currentItem.equals(getShopButton(p))) {
			CustomShop customShop = new CustomShop(villagerName);
			customShop.openShop(p);
		}
	}

	private static ItemStack getBuyerButton(Player p) {
		ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
		ItemStackUtil.setDispName(itemStack, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage(p, "売却"));
		ItemStackUtil.setLore(itemStack, Arrays.asList(Message.getMessage(p, "アイテムを売却する")));
		return itemStack;
	}

	private static ItemStack getShopButton(Player p) {
		ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
		ItemStackUtil.setDispName(itemStack, ChatColor.WHITE + "" + ChatColor.BOLD + Message.getMessage(p, "購入"));
		ItemStackUtil.setLore(itemStack, Arrays.asList(Message.getMessage(p, "アイテムを購入する")));
		return itemStack;
	}
}
