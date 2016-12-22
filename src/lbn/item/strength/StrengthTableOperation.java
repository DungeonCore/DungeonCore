package lbn.item.strength;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import lbn.player.CraftTableViewManager;
import lbn.player.crafttable.CraftTableType;
import lbn.util.ItemStackUtil;

public class StrengthTableOperation {

	public static ItemStack yellowGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4);
	public static ItemStack redGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
	{
		ItemStackUtil.setDispName(yellowGlass, "  ");
		ItemStackUtil.setDispName(redGlass,  ChatColor.RED + ChatColor.BOLD.toString() + "INFORMATION");
		ItemStackUtil.setLore(redGlass,  Arrays.asList(ChatColor.GREEN + "強化可能なアイテムをこの赤いガラスの隣のどちらかに置いてください。"));
	}

	public static void openStrengthTable(final Player p) {
		InventoryView openWorkbench = CraftTableViewManager.openWorkbench(p, CraftTableType.STREAGTH_TABLE);
		final CraftingInventory inv = (CraftingInventory) openWorkbench.getTopInventory();
		setInitInv(inv);
	}

	static ItemStack[] items = new ItemStack[]{yellowGlass, yellowGlass, yellowGlass, null, redGlass, null, yellowGlass, yellowGlass, yellowGlass};
	public static void setInitInv(final CraftingInventory inv) {
		for (int i = 0; i < items.length; i++) {
			ItemStack item = inv.getItem(i + 1);
			if (item == null || item.getType() == Material.AIR) {
				inv.setItem(i + 1, items[i]);
			}
		}

		//アイテムを１つ減少させる
		inv.setItem(4, ItemStackUtil.getDecrement(inv.getItem(4)));
		inv.setItem(6, ItemStackUtil.getDecrement(inv.getItem(6)));
	}

	public static boolean isOpenStrengthTable(HumanEntity humanEntity) {
		return CraftTableViewManager.isOpenCraftingTable((Player) humanEntity, CraftTableType.STREAGTH_TABLE);
	}

	public static void removeGlass(InventoryCloseEvent e) {
		int size = e.getInventory().getSize();

		//黄色の板ガラスを削除
		Inventory inv = e.getInventory();
		for (int i = 0; i < Math.min(9, size); i++) {
			inv.removeItem((StrengthTableOperation.yellowGlass));
		}

		if (size <= 5) {
			return;
		}

		//赤の板ガラスを削除
		ItemStack item = inv.getItem(5);
		if (item == null) {
			return;
		}
		if (isRedGlass(item)) {
			inv.remove(item);
		}
	}

	public static boolean isYellowGlass(ItemStack i) {
		return yellowGlass.equals(i);
	}

	@SuppressWarnings("deprecation")
	public static boolean isRedGlass(ItemStack i) {
		if (i == null) {
			return false;
		}
		if (i.getType() == null) {
			return false;
		}
		if ( i.getData() == null) {
			return false;
		}
		return ItemStackUtil.getName(i).equals(ItemStackUtil.getName(redGlass)) && i.getType().equals(redGlass.getType()) && i.getData().getData() == redGlass.getData().getData();
	}
}
