package lbn.item.slot.table;

import java.util.ArrayList;

import lbn.dungeoncore.Main;
import lbn.item.attackitem.AttackItemStack;
import lbn.item.slot.SlotInterface;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SlotSetResultItemRunnable extends BukkitRunnable{
	 public SlotSetResultItemRunnable(CraftingInventory top, InventoryClickEvent e) {
		this.top = top;
		this.e = e;
		p = (Player) e.getWhoClicked();
	}
	 Player p;
	CraftingInventory top;
	 InventoryClickEvent e;

	@Override
	public void run() {
		if (e.getRawSlot() == 4 || e.getRawSlot() == 6) {
			Object[] slotItems = SlotSetTableOperation.getSlotItems(top);
			if (slotItems == null) {
				return;
			}
			AttackItemStack attackItem = (AttackItemStack)slotItems[0];
			SlotInterface magicStone = (SlotInterface) slotItems[1];

			SlotSetOperator slotSetOperator = new SlotSetOperator(attackItem, magicStone);
			//エラーがあるかチェックする
			String error = slotSetOperator.check();
			if (error != null) {
				sendError(error);
				return;
			}

			//アイテムをスロットにセットする
			slotSetOperator.setSlot();

			//成功確率をセットする
			ItemStack updateRedGlass = getUpdateRedGlass(slotSetOperator.getSuccessRate());
			top.setItem(5, updateRedGlass);

			ItemStack complate = attackItem.getItem();
			complate.setAmount(1);
			//アイテムをセットする
			top.setResult(complate);
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
		top.setItem(5, SlotSetTableOperation.redGlass);
		Message.sendMessage(p, msg);
	}

	protected ItemStack getUpdateRedGlass(double successChance) {
		ItemStack clone = SlotSetTableOperation.redGlass.clone();

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "・success");
		lore.add(ChatColor.GREEN.toString() + "   - " + successChance + "%");

		ItemStackUtil.setLore(clone, lore);

		return clone;
	}

	protected ItemStack getMaxLevelRedGlass() {
		ItemStack clone = SlotSetTableOperation.redGlass.clone();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED.toString() + ChatColor.BOLD +"これ以上強化できません");
		ItemStackUtil.setLore(clone, lore);
		return clone;
	}
}
