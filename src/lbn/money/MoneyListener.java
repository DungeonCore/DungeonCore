package lbn.money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lbn.dungeoncore.Main;
import lbn.item.GalionItem;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.money.buyer.Buyer;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.money.shop.CustomShop;
import lbn.money.shop.ShopItem;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MoneyListener implements Listener{

	@EventHandler
	public void onClickShop(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		String title = inventory.getTitle();
		if (inventory.getType() != InventoryType.CHEST || !title.endsWith("shop")) {
			return;
		}

		e.setCancelled(true);

		Player p = (Player) e.getWhoClicked();

		InventoryView view = e.getView();
		if (e.getClickedInventory() == null || e.getClickedInventory().equals(view.getBottomInventory())) {
			return;
		}

		if (ItemStackUtil.isEmpty(e.getCurrentItem())) {
			return;
		}

		CustomShop customShop = new CustomShop(title.replace(" shop", ""));

		ShopItem shopItem = customShop.fromShopItem(e.getCurrentItem(), true);
		if (shopItem == null) {
			Message.sendMessage(p, ChatColor.RED + "エラーが発生したためそのアイテムを購入できませんでした。");
			p.closeInventory();
			return;
		}

		//お金チェック
		if (GalionManager.getGalion(p) < shopItem.getPrice()) {
			Message.sendMessage(p, ChatColor.RED + "お金が足りないので購入できません。");
			return;
		}

		//インベントリチェック
		if (p.getInventory().firstEmpty() == -1) {
			Message.sendMessage(p, ChatColor.RED + "インベントリに空きがないので購入できません。");
			p.closeInventory();
			return;
		}

		//インベントリに追加する
		ItemStack buyItem = shopItem.getItem();
		buyItem.setAmount(shopItem.getCount());
		p.getInventory().addItem(buyItem);

		//お金の計算を行う
		GalionManager.addGalion(p, - shopItem.getPrice(), GalionEditReason.consume_shop);
	}

	@EventHandler
	public void onDragShop(InventoryDragEvent e) {
		Inventory inventory = e.getInventory();
		String title = inventory.getTitle();
		if (inventory.getType() != InventoryType.CHEST || !title.endsWith("shop")) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void onPickupMoney(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem().getItemStack();
		if (item.getType() == Material.GOLD_INGOT) {
			executeGalionItem(player);
		}
	}

	@EventHandler
	public void onClickMoney(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.GOLD_INGOT) {
			executeGalionItem(player);
		} else if (e.getCursor() != null && e.getCursor().getType() == Material.GOLD_INGOT) {
			executeGalionItem(player);
		}
	}

	@EventHandler
	public void onDragMoney(InventoryDragEvent e) {
		Player player = (Player) e.getWhoClicked();
		if ((e.getCursor() != null && e.getCursor().getType() == Material.GOLD_INGOT ) ||
				(e.getOldCursor().getType() != null && e.getOldCursor().getType() == Material.GOLD_INGOT)) {
			executeGalionItem(player);
		}
	}

	protected void executeGalionItem(Player player) {
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				HashMap<Integer, ? extends ItemStack> all = player.getInventory().all(Material.GOLD_INGOT);
				ArrayList<Integer> indexList = new ArrayList<Integer>();
				for (Entry<Integer, ? extends ItemStack> entry : all.entrySet()) {
					ItemInterface customItem = ItemManager.getCustomItem(entry.getValue());
					if (customItem != null && customItem instanceof GalionItem) {
						int galions = new GalionItem(entry.getValue()).getGalions();
						indexList.add(entry.getKey());
						GalionManager.addGalion(player, galions * entry.getValue().getAmount(), GalionEditReason.get_money_item);
					}
				}

				for (Integer integer : indexList) {
					player.getInventory().clear(integer);
				}
			}
		}.runTaskLater(Main.plugin, 2);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Buyer.onInteractInv(e);
		BuyerShopSelector.onSelect(e);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		Buyer.onInteractInv(e);
		BuyerShopSelector.onSelect(e);
	}

	@EventHandler
	public void onPrepareItem(PrepareItemCraftEvent e) {
		Buyer.onCraftItem(e);
	}
}
