package lbn.item.repair.repairTable;

import lbn.dungeoncore.Main;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.customItem.attackitem.AttackItemStack;
import lbn.item.slot.SlotInterface;
import lbn.player.CraftTableViewManager;
import lbn.player.crafttable.CraftTableType;
import lbn.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class RepairSetTableOperation {

	public static ItemStack outLineGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
	static{
		ItemStackUtil.setDispName(outLineGlass, "  ");
	}

	static ItemStack[] items = new ItemStack[]{outLineGlass, outLineGlass, outLineGlass, outLineGlass, null, outLineGlass, outLineGlass, outLineGlass, outLineGlass};

	public static void openSlotTable(final Player p) {
		InventoryView openWorkbench = CraftTableViewManager.openWorkbench(p, CraftTableType.SLOT_TABLE);
		final CraftingInventory inv = (CraftingInventory) openWorkbench.getTopInventory();
		setInitInv(inv);
	}

	public static void setInitInv(final CraftingInventory inv) {
		for (int i = 0; i < items.length; i++) {
			ItemStack item = inv.getItem(i + 1);
			if (item == null || item.getType() == Material.AIR) {
				inv.setItem(i + 1, items[i]);
			}
		}
	}

	public static boolean isOpenSlotTable(HumanEntity humanEntity) {
		return CraftTableViewManager.isOpenCraftingTable((Player) humanEntity, CraftTableType.SLOT_TABLE);
	}

	public static void removeGlass(InventoryCloseEvent e) {
		int size = e.getInventory().getSize();

		//灰色の板ガラスを削除
		Inventory inv = e.getInventory();
		for (int i = 0; i < Math.min(9, size); i++) {
			inv.removeItem((RepairSetTableOperation.outLineGlass));
		}

		if (size <= 5) {
			return;
		}

		//赤の板ガラスを削除
//		ItemStack item = inv.getItem(5);
//		if (item == null) {
//			return;
//		}
//		if (isRedGlass(item)) {
//			inv.remove(item);
//		}
	}

	public static boolean isGrayGlass(ItemStack i) {
		return outLineGlass.equals(i);
	}

	public static void inventoryClick (final InventoryClickEvent e) {
		if (!isOpenSlotTable(e.getWhoClicked())) {
			return;
		}

		//ガラスはクリックしてもキャンセルする
		ItemStack currentItem = e.getCurrentItem();
		if (isGrayGlass(currentItem)) {
			e.setCancelled(true);
			return;
		}

		//絶対に作業台が開いているはず
		if (!(e.getView().getTopInventory() instanceof CraftingInventory)) {
			return;
		}

		final CraftingInventory top = (CraftingInventory) e.getView().getTopInventory();
		if (e.getRawSlot() == 4 || e.getRawSlot() == 6) {
			new RepairSetResultItemRunnable(top, e).runTaskLater(Main.plugin, 1);
		} else if (e.getSlotType() == SlotType.RESULT) {
			Object[] slotItems = getSlotItems(top);
			if (slotItems == null) {
				return;
			}
			if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
				new CraeteRepairItemResultLater(top, e, (AttackItemStack)slotItems[0], (SlotInterface) slotItems[1]).runTaskLater(Main.plugin);
			} else {
				e.setCancelled(true);
			}
		}
	}

	/**
	 * 4番と6番にSlot関係のアイテムが存在してるならそれを返す。それ以外ならnullを返す
	 * @param top
	 * @return
	 */
	protected static Object[] getSlotItems(CraftingInventory top) {
		ItemStack item1 = top.getItem(4);
		ItemStack item2 = top.getItem(6);

		AttackItemStack attackItem = null;
		SlotInterface magicStone = null;

		ItemInterface customItem1 = ItemManager.getCustomItem(item1);
		ItemInterface customItem2 = ItemManager.getCustomItem(item2);

		if (AttackItemStack.getInstance(item1) != null && customItem2 instanceof SlotInterface)  {
			attackItem = AttackItemStack.getInstance(getCloneItem(item1));
			magicStone = (SlotInterface) customItem2;
		}else if (AttackItemStack.getInstance(item2) != null && customItem1 instanceof SlotInterface)  {
			attackItem = AttackItemStack.getInstance(getCloneItem(item2));
			magicStone = (SlotInterface) customItem1;
		} else {
			return null;
		}
		return new Object[]{attackItem, magicStone};
	}

	protected static ItemStack getCloneItem(ItemStack strengthItem) {
		ItemStack clone = strengthItem.clone();

		if (strengthItem.getType() == Material.BOW || ItemStackUtil.isSword(strengthItem)) {
			clone.setDurability((short) 0);
		} else {
			clone.setDurability(strengthItem.getDurability());
		}
		return clone;
	}

}
