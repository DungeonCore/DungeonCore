package lbn.item.attackitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.SlotManager;
import lbn.item.slot.AbstractSlot;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.slot.EmptySlot;
import lbn.item.strength.StrengthOperator;
import lbn.player.ItemType;
import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * 武器のみ
 * @author KENSUKE
 *
 */
public class AttackItemStack {
	protected AttackItemStack(ItemStack item, AbstractAttackItem itemInterface) {
		this.item = item;
		this.itemInterface = itemInterface;
	}

	ItemStack item;
	AbstractAttackItem itemInterface;

	static HashMap<ItemStack, AttackItemStack> cache = new HashMap<ItemStack, AttackItemStack>();

	public static AttackItemStack getInstance(ItemStack item) {
		ItemInterface customItem = ItemManager.getCustomItem(item);
		if (customItem == null) {
			return null;
		}
		if (customItem instanceof AbstractAttackItem) {
			AttackItemStack attackItemStack = getCache(item);
			return attackItemStack;
		}
		return null;
	}

	/**
	 * キャッシュから取得する
	 * @param item
	 * @return
	 */
	private static AttackItemStack getCache(ItemStack item) {
		if (cache.containsKey(item)) {
			return cache.get(item);
		}
		return new AttackItemStack(item, (AbstractAttackItem) ItemManager.getCustomItem(item));
	}

	public ItemStack getItem() {
		return item;
	}

	public AbstractAttackItem getItemInterface() {
		return itemInterface;
	}

	public int getStrengthLevel() {
		return StrengthOperator.getLevel(item);
	}

	public ItemType getItemType() {
		return itemInterface.getAttackType();
	}

	public int getAvailableLevel() {
		return itemInterface.getAvailableLevel();
	}

	ArrayList<SlotInterface> slotList = null;

	public ArrayList<SlotInterface> getUseSlot() {
		initSlot();
		return slotList;
	}

	protected void initSlot() {
		if (slotList != null) {
			return;
		}

		slotList = new ArrayList<SlotInterface>();
		for (String string : ItemStackUtil.getLore(item)) {
			SlotInterface slotByLore = SlotManager.getSlotByLore(string);
			if (slotByLore != null) {
				slotList.add(slotByLore);
			}
		}
	}

	public boolean removeSlot(SlotInterface slot) {
		initSlot();
		return slotList.remove(slotList.indexOf(slot)) != null;
	}

	public boolean addEmptySlot() {
		initSlot();

		if (slotList.size() < 5) {
			slotList.add(new EmptySlot());
			return true;
		}
		return false;
	}

	public boolean addSlot(SlotInterface slot) {
		initSlot();

		if (slotList.size() < 5) {
			slotList.add(slot);
			return true;
		}
		return false;
	}

	public boolean hasSlot(AbstractSlot slot) {
		initSlot();
		return slotList.contains(slot);
	}

	public void updateItem() {
		List<String> lore = ItemStackUtil.getLore(item);
		Iterator<String> iterator = lore.iterator();

		boolean slotFlg = false;
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (slotFlg) {
				iterator.remove();
				//改行が存在したらSLOT終了とする
				if (next.trim().equals("")) {
					break;
				}
			}

			if (next.contains("[SLOT]")) {
				iterator.remove();
				slotFlg = true;
			}
		}

		lore.add(ChatColor.GREEN + "[SLOT]");
		for (SlotInterface abstractSlot : slotList) {
			String[] slotLore = SlotManager.getLore(abstractSlot);
			for (String line : slotLore) {
				lore.add(line);
			}
		}
		lore.add("");
		ItemStackUtil.setLore(item, lore);

	}

}
