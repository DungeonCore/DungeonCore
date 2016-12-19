package lbn.item.repair.repairTable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import lbn.item.attackitem.AttackItemStack;
import lbn.item.slot.SlotInterface;
import lbn.util.Message;

public class RepairSetResultItemRunnable extends BukkitRunnable{
	 public RepairSetResultItemRunnable(CraftingInventory top, InventoryClickEvent e) {
		this.top = top;
		this.e = e;
		p = (Player) e.getWhoClicked();
	}
	 Player p;
	CraftingInventory top;
	 InventoryClickEvent e;
	 AttackItemStack attackItem;
	 SlotInterface magicStone;

	@Override
	public void run() {
		if (e.getRawSlot() == 4 || e.getRawSlot() == 6) {
			Object[] slotItems = RepairSetTableOperation.getSlotItems(top);
			if (slotItems == null) {
				return;
			}
			attackItem = (AttackItemStack)slotItems[0];
			magicStone = (SlotInterface) slotItems[1];


			RepairSetOperator slotSetOperator = new RepairSetOperator(attackItem, magicStone);

			//エラーがあるかチェックする
			String error = slotSetOperator.check();
			if (error != null) {
				sendError(error);
				return;
			}

			//アイテムをスロットにセットする
			slotSetOperator.setSlot();

			//成功確率をセットする
//			ItemStack updateRedGlass = getUpdateRedGlass(slotSetOperator.getSuccessRate());
//			top.setItem(5, updateRedGlass);

			//アイテムをセットする
			top.setResult(attackItem.getItem());
			//完成形を表示させる
			new BukkitRunnable() {
				@Override
				public void run() {
					p.updateInventory();
				}
			}.runTaskLater(Main.plugin, 2);
		}
	}

	protected void sendError(String msg) {
//		top.setItem(5, RepairSetTableOperation.redGlass);
		Message.sendMessage(p, msg);
	}

//	protected ItemStack getUpdateRedGlass(double successChance) {
//		ItemStack clone = RepairSetTableOperation.redGlass.clone();
//
//		ArrayList<String> lore = new ArrayList<String>();
//		lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "・success");
//		lore.add(ChatColor.GREEN.toString() + "   - " + successChance + "%");
//
//		ItemStackUtil.setLore(clone, lore);
//
//		return clone;
//	}

//	protected ItemStack getMaxLevelRedGlass() {
//		ItemStack clone = RepairSetTableOperation.redGlass.clone();
//		ArrayList<String> lore = new ArrayList<String>();
//		lore.add(ChatColor.RED.toString() + ChatColor.BOLD +"これ以上強化できません");
//		ItemStackUtil.setLore(clone, lore);
//		return clone;
//	}
}
