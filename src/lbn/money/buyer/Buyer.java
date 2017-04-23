package lbn.money.buyer;

import java.util.ArrayList;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.dungeoncore.Main;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.customItem.other.GalionItem;
import lbn.player.CraftTableViewManager;
import lbn.player.crafttable.CraftTableType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Buyer {
	public static void onOpen(Player p) {
		CraftTableViewManager.openWorkbench(p, CraftTableType.BUYER_TABLE);
	}

	public static boolean isOpenBuyer(Player p) {
		return CraftTableViewManager.isOpenCraftingTable(p, CraftTableType.BUYER_TABLE);
	}

	/**
	 * Buyer用のクラフトテーブルでは通常のクラフトは行えない
	 * @param event
	 */
	public static void onCraftItem(PrepareItemCraftEvent event) {
		Player p = (Player) event.getView().getPlayer();
		if (!isOpenBuyer(p)) {
			return;
		}

		ItemStack result = event.getRecipe().getResult();
		//生成されたアイテムがお金なら何もしない
		if (GalionItem.getInstance(0).isThisItem(result)) {
			return;
		}

		//もし別なアイテムが生成されたなら削除する
		new BukkitRunnable() {
			@Override
			public void run() {
				event.getInventory().setResult(new ItemStack(Material.AIR));
				p.updateInventory();
			}
		}.runTaskLater(Main.plugin, 1);
	}

	/**
	 * 材料欄をクリックした場合はお金を計算し、resultをクリックした場合は全ての材料欄のアイテムを削除する
	 * @param event
	 */
	public static void onInteractInv(InventoryInteractEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (!isOpenBuyer(p)) {
			return;
		}

		InventoryView view = event.getView();
		Inventory topInventory = view.getTopInventory();

		//見ているものがクラフティングテーブルでないことは起こり得ないはず
		if (topInventory.getType() != InventoryType.WORKBENCH) {
			//強制的にインベントリを閉じる
			CraftTableViewManager.forceCloseInventory(p);
			//起こり得ないのでエラーにする
			new LbnRuntimeException("crafting table manager system is dont work!! type:" + topInventory.getType()).printStackTrace();
			return;
		}

		//クリック以外は認めない
		if (!(event instanceof InventoryClickEvent)) {
			event.setCancelled(true);
			return;
		}

		InventoryClickEvent e = (InventoryClickEvent) event;

		CraftingInventory crafting = (CraftingInventory) topInventory;
		//お金を計算し、セットする
		if (1 <= e.getRawSlot() && e.getRawSlot() <= 9) {
			setGalions(crafting, e);
		} else if (e.getSlotType() == SlotType.RESULT) {
			if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
				allClearMaterial(crafting, e);
			} else {
				e.setCancelled(true);
			}
		}
	}

	private static void allClearMaterial(CraftingInventory crafting, InventoryClickEvent e) {
		//生成されたアイテムがお金なら何もしない
		if (!GalionItem.getInstance(0).isThisItem(crafting.getResult())) {
			return;
		}

		//マテリアルリスト
		ArrayList<ItemStack> deleteList = new ArrayList<ItemStack>();

		//setGalionsが必要か？
		ItemStack[] matrix = crafting.getMatrix();
		for (ItemStack itemStack : matrix) {
			ItemInterface customItem = ItemManager.getCustomItem(itemStack);
			//カスタムアイテムでないなら削除する
			if (customItem == null) {
				//削除する
				deleteList.add(itemStack);
				continue;
			}
			int buyPrice = customItem.getBuyPrice(itemStack);
			if (buyPrice == -1) {
				continue;
			} else {
				deleteList.add(itemStack);
			}
		}

		//素材を削除する
		new BukkitRunnable() {
			@Override
			public void run() {
				for (ItemStack itemStack : matrix) {
					crafting.remove(itemStack);
				}
			}
		}.runTaskLater(Main.plugin, 1);
	}

	/**
	 * 材料をすべて調べてGalionsをセットする
	 * @param crafting
	 * @param e
	 */
	private static void setGalions(CraftingInventory crafting, InventoryClickEvent e) {
		new BukkitRunnable() {

			//押した直後にアイテムを検出する
			@Override
			public void run() {
				ItemStack[] matrix = crafting.getMatrix();

				boolean emptyItemFlg = true;

				int galions = 0;
				for (ItemStack itemStack : matrix) {
					//アイテムが設置されていればTRUE
					if (itemStack != null && itemStack.getType() != Material.AIR) {
						emptyItemFlg = false;
					} else {
						continue;
					}

					ItemInterface customItem = ItemManager.getCustomItem(itemStack);

					//カスタムアイテムでないなら0Galionsなので無視する
					if (customItem == null) {
						continue;
					}

					int buyPrice = customItem.getBuyPrice(itemStack);
					if (buyPrice == -1) {
						continue;
					}
					//TODO バグで１つしかアイテムが減らないことがあるので要注意。その場合は下の処理をコメントアウトする
					buyPrice *= itemStack.getAmount();
					galions += buyPrice;
				}

				//売上が0より大きいならアイテムをセットする
				if (galions >= 0 && !emptyItemFlg) {
					crafting.setResult(GalionItem.getInstance(galions).getItem());
					((Player)e.getWhoClicked()).updateInventory();
				}

			}
		}.runTaskLater(Main.plugin, 1);
	}

}
