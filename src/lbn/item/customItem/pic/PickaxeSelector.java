package lbn.item.customItem.pic;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.common.menu.MenuSelectorInterface;
import lbn.common.menu.MenuSelectorManager;
import lbn.item.ItemInterface;
import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;
import lbn.player.PlayerChecker;

public class PickaxeSelector extends AbstractItem implements RightClickItemable {

	public PickaxeSelector(AbstractPickaxe pickaxe) {
		this.pickaxe = pickaxe;
		pickSelectorMenu = new PickSelectorMenu(pickaxe);
		MenuSelectorManager.regist(pickSelectorMenu);
	}

	AbstractPickaxe pickaxe;
	private PickSelectorMenu pickSelectorMenu;

	@Override
	public String getItemName() {
		return "ピッケルセレクター : " + pickaxe.getMaterialName() + "のピッケル";
	}

	@Override
	public String getId() {
		return "item_selector_" + pickaxe.getGiveItemId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 0;
	}

	@Override
	protected Material getMaterial() {
		return pickaxe.getItemStackBase().getType();
	}

	@Override
	public String[] getDetail() {
		return new String[] { "ピッケルのセレクター。運営のみ使用可能" };
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		pickSelectorMenu.open(e.getPlayer());
	}

	class PickSelectorMenu implements MenuSelectorInterface {
		AbstractPickaxe pickaxe;

		public PickSelectorMenu(AbstractPickaxe pickaxe) {
			this.pickaxe = pickaxe;
		}

		@Override
		public void open(Player p) {
			// もし、Playerなら何もしない
			if (PlayerChecker.isNormalPlayer(p)) {
				return;
			}

			Inventory createInventory = Bukkit.createInventory(null, 9 * 2, getTitle());
			List<ItemInterface> allLevelPick = pickaxe.getAllLevelPick();
			// 全てのピッケルを表示する
			for (ItemInterface itemInterface : allLevelPick) {
				createInventory.addItem(itemInterface.getItem());
			}
			p.openInventory(createInventory);
		}

		@Override
		public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
			// もし、Playerなら何もしない
			if (PlayerChecker.isNormalPlayer(p)) {
				return;
			}

			if (item == null || item.getType() == Material.AIR) {
				return;
			}
			p.setItemInHand(item);
			p.closeInventory();
		}

		@Override
		public String getTitle() {
			return "pick_selec_" + pickaxe.getGiveItemId();
		}

	}
}
