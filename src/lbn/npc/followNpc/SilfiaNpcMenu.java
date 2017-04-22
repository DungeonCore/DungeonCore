package lbn.npc.followNpc;

import lbn.common.menu.MenuSelectorInterface;
import lbn.util.ItemStackUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SilfiaNpcMenu implements MenuSelectorInterface {

	private static final ItemStack close = ItemStackUtil.getItem("メニューを閉じる", Material.WOOL, (byte) 14, "");
	private static final ItemStack hideNpc = ItemStackUtil.getItem("NPCを非表示にする", Material.WOOL, (byte) 1, "");

	@Override
	public void open(Player p) {
		Inventory createInventory = Bukkit.createInventory(null, 9 * 3, getTitle());
		createInventory.setItem(11, hideNpc);
		createInventory.setItem(15, close);

		p.openInventory(createInventory);
	}

	@Override
	public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
		if (close.equals(item)) {
			p.closeInventory();
		} else if (hideNpc.equals(item)) {
			FollowerNpcManager.despawn(p);
		}
	}

	@Override
	public String getTitle() {
		return "shilfia_npc_menu";
	}

}
