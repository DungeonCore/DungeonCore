package main.item;

import java.util.HashMap;

import main.item.slot.SlotInterface;
import main.item.slot.slot.AddEmptySlotItem;
import main.item.slot.slot.AddEmptySlotItem2;
import main.item.slot.slot.EmptySlot;
import main.item.slot.slot.RemoveUnavailableSlot;
import main.item.slot.slot.RemoveUnavailableSlot2;
import main.item.slot.slot.UnavailableSlot;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public class SlotManager {
	static HashMap<String, String> idNameMap = new HashMap<String, String>();
	static HashMap<String, SlotInterface> slotIDMap = new HashMap<String, SlotInterface>();

	static {
		registSlot(new UnavailableSlot());
		registSlot(new EmptySlot());
		ItemManager.registItem(new AddEmptySlotItem());
		ItemManager.registItem(new RemoveUnavailableSlot());
		ItemManager.registItem(new AddEmptySlotItem2());
		ItemManager.registItem(new RemoveUnavailableSlot2());
	}

	public static void registSlot(SlotInterface slot) {
		idNameMap.put(slot.getSlotName(), slot.getId());
		slotIDMap.put(slot.getId(), slot);
	}

	//使わないと思うから一旦コメントアウト
//	public static AbstractSlot getSlotByName(String name) {
//		Integer integer = idNameMap.get(name);
//		if (integer == null) {
//			return null;
//		}
//
//		return slotIDMap.get(integer);
//	}

	/**
	 * Loreからスロットを取得
	 * @param line
	 * @return
	 */
	public static SlotInterface getSlotByLore(String line) {
		if (!isSlotTitleLore(line)) {
			return null;
		}

		line = line.substring(line.indexOf("id:") + 3);
		return slotIDMap.get(line);
	}

	/**
	 * スロットのためのLoreを取得する
	 * @param slot
	 * @return
	 */
	public static String[] getLore(SlotInterface slot) {
		String[] lore = new String[1];
		//表示させないため一旦コメントアウト
//		String detail = slot.getSlotDetail();
//		String detail = null;
//		String[] lore;
//		if (detail == null) {
//			lore = new String[1];
//		}
//		else {
//			lore = new String[2];
//			lore[1] = StringUtils.join(new Object[]{ChatColor.DARK_AQUA, "        - ", slot.getSlotDetail()});
//		}
		lore[0] = StringUtils.join(new Object[]{slot.getNameColor(), "    ■ ", slot.getSlotName(), ChatColor.BLACK, "id:", slot.getId()});
		return lore;
	}
//
	/**
	 * スロットTile文ならTrue
	 * @param line
	 * @return
	 */
	public static boolean isSlotTitleLore(String line) {
		if (line == null) {
			return false;
		}
		return line.contains("    ■ ") && line.contains("id:");
	}
//
//	public static boolean removeSlot(ItemStack item, AbstractSlot slot) {
//		List<String> loreList = ItemStackUtil.getLore(item);
//		Iterator<String> lore = loreList.iterator();
//
//		boolean thisSlotFlg = false;
//
//		while (lore.hasNext()) {
//			String string  = lore.next();
//			AbstractSlot slotByLore = getSlotByLore(string);
//			if (slotByLore == null) {
//				continue;
//			}
//
//			//見つけたフラグが立っていたら次の行に説明がある可能性がある
//			if (thisSlotFlg) {
//				//説明だったら削除
//				if (string.contains("        - ")) {
//					lore.remove();
//				}
//				break;
//			}
//
//			//目的のスロットを見つけたら削除する
//			if (slotByLore.equals(slot)) {
//				//見つけたフラグを立てておく
//				thisSlotFlg = true;
//				lore.remove();
//			}
//		}
//
//		ItemStackUtil.setLore(item, loreList);
//		return thisSlotFlg;
//	}
//
//	public static boolean addEmptySlot(ItemStack item) {
//		if (ItemManager.getCustomItem(item) == null) {
//			return false;
//		}
//
//		List<String> lore = ItemStackUtil.getLore(item);
//
//
//	}
}
