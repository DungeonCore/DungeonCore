package lbn.item.slot.table;

import java.util.Random;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.dungeoncore.Main;
import lbn.item.customItem.attackitem.AttackItemStack;
import lbn.item.slot.SlotInterface;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CraeteSlotItemResultLater extends BukkitRunnable {
	static Random rnd = new Random();

	public CraeteSlotItemResultLater(CraftingInventory top, InventoryClickEvent e, AttackItemStack slotItems,
			SlotInterface slot) {
		this.top = top;
		this.e = e;
		this.attackItem = slotItems;
		this.magicStone = slot;
	}

	CraftingInventory top;
	InventoryClickEvent e;
	AttackItemStack attackItem;
	SlotInterface magicStone;

	boolean canStrength = true;

	@Override
	public void run() {
		SlotSetOperator slotSetOperator = new SlotSetOperator(attackItem, magicStone);
		String error = slotSetOperator.check();
		if (error != null) {
			// エラーがあるときはロールバックする
			ItemStack cursor = e.getCursor();
			slotSetOperator.rollback(cursor);
			new LbnRuntimeException("error while set slot!!").printStackTrace();
			canStrength = false;
			((Player) e.getWhoClicked()).updateInventory();
		}

		double successPer = slotSetOperator.getSuccessRate();

		boolean isSuccess = (rnd.nextInt(100) + 1 <= successPer);

		// クリエでなら絶対成功
		if (((Player) e.getWhoClicked()).getGameMode() == GameMode.CREATIVE) {
			isSuccess = true;
			((Player) e.getWhoClicked()).sendMessage(ChatColor.YELLOW + "クリエイティブモードなので絶対に成功します。");
		}
		// 成功
		if (isSuccess) {
			// 成功の時は何もしない
			((Player) e.getWhoClicked()).sendMessage(ChatColor.GREEN + slotSetOperator.getScuessComment());
			((Player) e.getWhoClicked()).playSound(((Player) e.getWhoClicked()).getLocation(), Sound.ANVIL_USE, 1, 1);
			// 失敗
		} else {
			try {
				((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + slotSetOperator.getFailureComment());
				((Player) e.getWhoClicked()).playSound(((Player) e.getWhoClicked()).getLocation(), Sound.ITEM_BREAK, 1f,
						1f);
				ItemStack cursor = e.getCursor();
				slotSetOperator.rollback(cursor);
				canStrength = false;
				((Player) e.getWhoClicked()).updateInventory();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		canStrength = true;
	}

	public synchronized BukkitTask runTaskLater(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
		BukkitTask runTaskLater = super.runTaskLater(plugin, 1);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!canStrength) {
					return;
				}

				// クラフト欄を初期化する
				if ((CraftingInventory) e.getView().getTopInventory() instanceof CraftingInventory) {
					SlotSetTableOperation.setInitInv((CraftingInventory) e.getView().getTopInventory());
					((Player) e.getWhoClicked()).updateInventory();
				}
			}
		}.runTaskLater(Main.plugin, 3);

		return runTaskLater;
	}

}
