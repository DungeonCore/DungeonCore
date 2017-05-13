package net.l_bulb.dungeoncore.money;

import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.money.buyer.Buyer;
import net.l_bulb.dungeoncore.money.shop.CustomShop;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import net.md_5.bungee.api.ChatColor;

public class BuyerShopSelector {
  private static final String SHOP_MENU = ChatColor.WHITE + "- Shop - ";

  public static void onOpen(Player p, String villagerID) {
    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, SHOP_MENU + villagerID);
    createInventory.setItem(11, getShopButton(p));
    createInventory.setItem(15, getBuyerButton(p));
    p.openInventory(createInventory);
  }

  public static void onSelect(InventoryInteractEvent e) {
    InventoryView view = e.getView();
    String title = view.getTitle();
    // もしセレクト画面でないなら何もしない
    if (title == null || !title.contains(SHOP_MENU)) { return; }
    e.setCancelled(true);
    // クリックでないなら何もしない
    if (!(e instanceof InventoryClickEvent)) { return; }

    String villagerID = title.replace(SHOP_MENU, "");

    InventoryClickEvent event = (InventoryClickEvent) e;

    Player p = (Player) event.getWhoClicked();

    ItemStack currentItem = event.getCurrentItem();
    if (currentItem != null && currentItem.equals(getBuyerButton(p))) {
      Buyer.onOpen(p);
    } else if (currentItem != null && currentItem.equals(getShopButton(p))) {
      CustomShop customShop = new CustomShop(villagerID);
      customShop.openShop(p);
    }
  }

  private static ItemStack getBuyerButton(Player p) {
    return ItemStackUtil.getItem(MessageFormat.format("{0}{1}売却", ChatColor.WHITE, ChatColor.BOLD), Material.GOLD_INGOT, ChatColor.GREEN
        + "アイテムを売却する");
  }

  private static ItemStack getShopButton(Player p) {
    return ItemStackUtil.getItem(MessageFormat.format("{0}{1}購入", ChatColor.WHITE, ChatColor.BOLD), Material.NETHER_BRICK, ChatColor.GREEN
        + "アイテムを購入する");
  }
}
