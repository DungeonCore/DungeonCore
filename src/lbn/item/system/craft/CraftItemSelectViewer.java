package lbn.item.system.craft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbn.NbtTagConst;
import lbn.common.menu.MenuSelectorInterface;
import lbn.common.menu.MenuSelectorManager;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.itemInterface.CraftItemable;
import lbn.npc.villagerNpc.VillagerNpc;
import lbn.npc.villagerNpc.VillagerNpcManager;
import lbn.util.ItemStackUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CraftItemSelectViewer implements MenuSelectorInterface {
	private static final String TITLE = "アイテム制作";

	static {
		MenuSelectorManager.regist(new CraftItemSelectViewer());
	}

	public static void openTest(Player p, String villagerID) {
		VillagerNpc villager = VillagerNpcManager.getVillagerNpcById(villagerID);
		String data = villager.getData();
		Location locationByString = AbstractSheetRunable.getLocationByString(data);
		Block block = locationByString.getBlock();
		Inventory inventory = getInventory(block);
		open(p, Arrays.asList(inventory), 0);
	}

	/**
	 * 指定したクラフト後のアイテムが入ったインベントリを開く
	 * 
	 * @param p
	 * @param invList
	 * @param index
	 */
	public static void open(Player p, String villagerId, int index) {
		VillagerNpc villager = VillagerNpcManager.getVillagerNpcById(villagerId);
		if (villager == null) {
			p.sendMessage("指定された村人がいません。" + villagerId);
			return;
		}
		String dataList = villager.getData();
		if (dataList == null) {
			p.sendMessage("指定された村人のデータがありません。村人：" + villagerId);
			return;
		}

		ArrayList<Inventory> validInvList = new ArrayList<Inventory>();

		// 正しいインベントを取得する
		for (String data : dataList.split("&")) {
			Location location = AbstractSheetRunable.getLocationByString(data.trim());
			if (location == null) {
				continue;
			}
			Block block = location.getBlock();
			Inventory inventory = getInventory(block);
			if (inventory == null) {
				continue;
			}
			validInvList.add(inventory);
		}

		if (validInvList.size() == 0) {
			p.sendMessage("開けるクラフト画面が存在しません。村人：" + villagerId);
			return;
		}

		open(p, validInvList, index % validInvList.size());
	}

	static ItemStack AIR = new ItemStack(Material.AIR);

	/**
	 * 指定したクラフト後のアイテムが入ったインベントリを開く
	 * 
	 * @param p
	 * @param invList
	 * @param index
	 */
	public static void open(Player p, List<Inventory> invList, int index) {
		// 設定したインベントリ
		Inventory inventory = invList.get(index);

		Inventory itemViewer = Bukkit.createInventory(null, inventory.getSize(), TITLE);
		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++) {
			// アイテムが無い時は無視する
			if (contents[i] == null) {
				continue;
			}

			// クラフトできるアイテムを取得する
			ItemInterface customItem = ItemManager.getCustomItem(contents[i]);
			// クラフト出来るアイテムでないなら無視
			if (customItem == null) {
				// オリジナルアイテムでないならそのまま入れる
				itemViewer.setItem(i, contents[i]);
			} else {
				// クラフト出来るアイテムならセットする
				if (ItemManager.isImplemental(CraftItemable.class, customItem)) {
					itemViewer.setItem(i, CraftItemSelectViewerItems.getViewItem((CraftItemable) customItem));
				}
			}
		}

		p.openInventory(itemViewer);
	}

	/**
	 * 指定したチェストのインベントリを開く。もしダブルチェストならダブルチェストで開く
	 * 
	 * @param b
	 * @return
	 */
	public static Inventory getInventory(Block b) {
		if (b.getType().equals(Material.CHEST)) {
			Chest c = (Chest) b.getState();
			Inventory inv = c.getInventory();

			if (inv.getSize() == 9 * 6) {
				return (DoubleChestInventory) inv;
			} else {
				return inv;
			}
		}

		return null;
	}

	@Override
	public void open(Player p) {
		// 直接は開けられない
		p.sendMessage("この操作はサポートされていません");
	}

	@Override
	public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
		String nbtTag = ItemStackUtil.getNBTTag(item, NbtTagConst.THELOW_ITEM_ID_FOR_CRAFT);
		// クラフトできないアイテムを選択しているので何もしない
		if (nbtTag == null || nbtTag.isEmpty() || item.getType() == Material.BARRIER) {
			return;
		}

		CraftItemable customItem = ItemManager.getCustomItem(CraftItemable.class, item);
		// nullはありえないが念のため
		if (customItem == null) {
			new RuntimeException("このアイテムはクラフト出来ません。id:" + nbtTag).printStackTrace();
			return;
		}

		TheLowCraftRecipeInterface craftRecipe = customItem.getCraftRecipe();
		// 素材を持っているか確認
		if (craftRecipe.hasAllMaterial(p, true)) {
			craftRecipe.openCraftingViewer(p, customItem);
		} else {
			// 書き換える
			CraftItemSelectViewerItems.addDontHasMaterial(item);
			e.setCurrentItem(item);
			p.updateInventory();
		}
	}

	@Override
	public String getTitle() {
		return TITLE;
	}
}

class InventoryViewImplement extends InventoryView {
	public InventoryViewImplement(Inventory top, Inventory bottom, Player p) {
		this.top = top;
		this.bottom = bottom;
		this.p = p;
	}

	Inventory top;
	Inventory bottom;
	Player p;

	@Override
	public Inventory getTopInventory() {
		return top;
	}

	@Override
	public Inventory getBottomInventory() {
		return bottom;
	}

	@Override
	public HumanEntity getPlayer() {
		return p;
	}

	@Override
	public InventoryType getType() {
		return InventoryType.CHEST;
	}

}
