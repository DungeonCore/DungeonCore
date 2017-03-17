package lbn.item.craft;

import java.util.Arrays;
import java.util.List;

import lbn.common.menu.MenuSelectorInterface;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.item.ItemManager;
import lbn.item.itemInterface.CraftItemable;
import lbn.npc.NpcManager;
import lbn.npc.VillagerNpc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CraftViewer implements MenuSelectorInterface{
	private static final String TITLE = "アイテム制作";

	public static void openTest(Player p, String villagerID) {
		VillagerNpc villager = NpcManager.getVillagerNpcById(villagerID);
		String data = villager.getData();
		Location locationByString = AbstractSheetRunable.getLocationByString(data);
		Block block = locationByString.getBlock();
		Inventory inventory = getInventory(block);
		open(p, Arrays.asList(inventory), 0);
	}

	static ItemStack AIR = new ItemStack(Material.AIR);

	public static void open(Player p, List<Inventory> invList, int index) {
		//設定したインベントリ
		Inventory inventory = invList.get(index);

		Inventory itemViewer = Bukkit.createInventory(null, inventory.getSize(), TITLE);
		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++) {
			//アイテムが無い時は無視する
			if (contents[i] == null) {
				continue;
			}

			//クラフトできるアイテムを取得する
			CraftItemable customItem = ItemManager.getCustomItem(CraftItemable.class, contents[i]);
			if (customItem == null) {
				return;
			}
			itemViewer.setItem(i, contents[i]);
		}

		p.openInventory(itemViewer);
	}

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

	}

	@Override
	public void onSelectItem(Player p, ItemStack item) {

	}

	@Override
	public String getTitle() {
		return TITLE;
	}
}

class InventoryViewImplement extends InventoryView{
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
