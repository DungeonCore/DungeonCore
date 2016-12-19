package main.item;

import java.util.Collection;

import main.item.setItem.SetItemInterface;
import main.item.setItem.SetItemManager;
import main.item.setItem.SetItemParts;
import main.item.setItem.SetItemPartsType;
import main.lbn.Main;
import main.util.ItemStackUtil;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SetItemListner implements Listener{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (((Player) e.getWhoClicked()).getGameMode() == GameMode.CREATIVE) {
			return;
		}

		//クリックした場所がSetItemの可能性がある場合
		if (SetItemPartsType.getTypeBySlot(e.getSlot()) != null && e.getClickedInventory().getType() == InventoryType.PLAYER) {
			//クリックしたアイテムがSETITEMのとき
			if (SetItemManager.isSetItem(e.getCurrentItem()) || SetItemManager.isSetItem(e.getCursor())) {
				//アイテムを置き終わった後のデータを見たいので２tick後に実行する
				new BukkitRunnable() {
					@Override
					public void run() {
						SetItemManager.updateAllSetItem((Player) e.getWhoClicked());
					}
				}.runTaskLater(Main.plugin, 2);
			}
		}
	}

	@EventHandler
	public void changeGameMode(PlayerGameModeChangeEvent e) {
		GameMode newGameMode = e.getNewGameMode();
		if (newGameMode != GameMode.CREATIVE) {
			SetItemManager.updateAllSetItem(e.getPlayer());
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		SetItemManager.updateAllSetItem((Player) player);
	}

	@EventHandler
	public void onDamage(final EntityDamageEvent e) {
		//装備をきた後に処理を行う
		new BukkitRunnable() {
			@Override
			public void run() {
				if (isNeedOnBreakCheck(e)) {
					SetItemManager.updateAllSetItem((Player) e.getEntity());
				}
			}
		}.runTaskLater(Main.plugin, 1);
		return;
	}

	private boolean isNeedOnBreakCheck(EntityDamageEvent e) {
		boolean checkFlg = false;
		Entity entity = e.getEntity();
		if (entity.getType() != EntityType.PLAYER) {
			return false;
		}

		//装備系のセット装備をつけているかどうか調べる
		Collection<SetItemInterface> setItemList = SetItemManager.getWearSetItemTypeNotCheck((Player) entity);
		outerloop:
		for (SetItemInterface setItemInterface : setItemList) {
			for (SetItemPartsType partsType : setItemInterface.getFullSetItem().keySet()) {
				ItemStack equipItem = partsType.getItemStackByParts((Player) entity);
				if (ItemStackUtil.isEmpty(equipItem)) {
					checkFlg = true;
					break outerloop;
			}
		}

			//1つでもパーツがかけている時だけチェックを行う
			for (ItemStack item : ((Player)e.getEntity()).getInventory().getArmorContents()) {
				if (item == null || item.getType() == Material.AIR) {
					checkFlg = true;
					break;
				}
			}

		}
		return checkFlg;
	}

	@EventHandler
	public void quit(PlayerQuitEvent e) {
		SetItemManager.removeAll(e.getPlayer());
	}

	@EventHandler
	public void click(final PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		if (ItemStackUtil.isEmpty(item)) {
			return;
		}
		boolean checkFlg = false;

		//クリックした部分のパーツが存在しない
		Collection<SetItemPartsType> partsTypeList = SetItemManager.getPartsTypeListByMaterial(item.getType());
		for (SetItemPartsType setItemPartsType : partsTypeList) {
			//もし装備するパーツでないならスキップする
			if (!setItemPartsType.isEquipParts()) {
				continue;
			}
			//該当箇所にあるパーツを取得
			ItemStack equipItem = setItemPartsType.getItemStackByParts(e.getPlayer());
			//もし何もなければ装備したものとする
			if (ItemStackUtil.isEmpty(equipItem)) {
				checkFlg = true;
				break;
			}
		}

		if (!checkFlg) {
			return;
		}

		String setitemName = SetItemParts.getSetItemNameByItem(item);
		if (setitemName != null ) {
			checkFlg = true;
		}

		if (checkFlg) {
			//装備をきた後に処理を行う
			new BukkitRunnable() {
				@Override
				public void run() {
					SetItemManager.updateAllSetItem(e.getPlayer());
				}
			}.runTaskLater(Main.plugin, 1);
		}
	}

}
