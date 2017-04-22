package lbn.item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import lbn.item.customItem.other.AddEmptySlotItem;
import lbn.item.customItem.other.AddEmptySlotItem2;
import lbn.item.customItem.other.RemoveUnavailableSlot;
import lbn.item.customItem.other.RemoveUnavailableSlot2;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.EmptySlot;
import lbn.item.slot.slot.UnavailableSlot;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import com.google.common.collect.HashMultimap;

public class SlotManager {
	static HashMap<String, String> idNameMap = new HashMap<String, String>();
	static HashMap<String, SlotInterface> slotIDMap = new HashMap<String, SlotInterface>();
	static HashMultimap<SlotLevel, SlotInterface> levelSlotMap = HashMultimap.create();

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
		levelSlotMap.put(slot.getLevel(), slot);
	}

	// 使わないと思うから一旦コメントアウト
	// public static AbstractSlot getSlotByName(String name) {
	// Integer integer = idNameMap.get(name);
	// if (integer == null) {
	// return null;
	// }
	//
	// return slotIDMap.get(integer);
	// }

	/**
	 * Loreからスロットを取得
	 * 
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
	 * 
	 * @param slot
	 * @return
	 */
	public static String[] getLore(SlotInterface slot) {
		String[] lore = new String[1];
		// 表示させないため一旦コメントアウト
		// String detail = slot.getSlotDetail();
		// String detail = null;
		// String[] lore;
		// if (detail == null) {
		// lore = new String[1];
		// }
		// else {
		// lore = new String[2];
		// lore[1] = StringUtils.join(new Object[]{ChatColor.DARK_AQUA, " - ",
		// slot.getSlotDetail()});
		// }
		lore[0] = StringUtils.join(new Object[] { slot.getNameColor(), "    ■ ", slot.getSlotName(), ChatColor.BLACK,
				"id:", slot.getId() });
		return lore;
	}

	//
	/**
	 * スロットTile文ならTrue
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isSlotTitleLore(String line) {
		if (line == null) {
			return false;
		}
		return line.contains("    ■ ") && line.contains("id:");
	}

	/**
	 * 指定したLevelからそれにあったMagicStoneを取得する
	 * 
	 * @param level
	 * @return
	 */
	public static Collection<SlotInterface> getSlotListByLevel(SlotLevel level) {
		Set<SlotInterface> set = levelSlotMap.get(level);
		if (set != null) {
			return set;
		} else {
			return Collections.emptyList();
		}
	}
}
