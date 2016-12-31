package lbn.quest.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lbn.dungeoncore.SpletSheet.QuestSheetRunnable;
import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuestSelectViewIcon {
	private final static String ID_PREFIX = ChatColor.BLACK + "q_view_id:";

	private static long lastUpdate = -1;

	public static boolean isThisItem(ItemStack item) {
		List<String> lore = ItemStackUtil.getLore(item);
		for (String string : lore) {
			if (string.contains(ID_PREFIX)) {
				return  true;
			}
		}
		return false;
	}

	static HashMap<Quest, ItemStack> itemCache = new HashMap<Quest, ItemStack>();

	public static ItemStack getItemStack(Quest q) {
		if (q == null || q.isNullQuest()) {
			return null;
		}

		//キャッシュ登録を始めるよりもあとにスプレットシートを登録したらキャッシュをを削除する
		long sheetLastUpdate = new QuestSheetRunnable(null).getLastUpdate();
		if (lastUpdate < sheetLastUpdate) {
			itemCache.clear();
			lastUpdate = sheetLastUpdate;
		}

		//キャッシュが残っていたらそれを使う
		if (itemCache.containsKey(q)) {
			return itemCache.get(q);
		}

		//Itemを作成
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.AQUA + q.getQuestDetail());
		lore.add(ID_PREFIX + q.getId());
		lore.add("[報酬]");
		for (String reword : q.getRewordText()) {
			lore.add(ChatColor.YELLOW + "   " + reword);
		}
		ItemStack item = ItemStackUtil.getItem(q.getName(), Material.BOOK, lore.toArray(new String[0]));
		itemCache.put(q, item);

		return item;
	}

	public static Quest getQuestByItem(ItemStack item) {
		if (!isThisItem(item)) {
			return null;
		}

		List<String> lore = ItemStackUtil.getLore(item);

		String id = null;
		for (String string : lore) {
			if (string.contains(ID_PREFIX)) {
				id = string.replace(ID_PREFIX, "").trim();
			}
		}

		Quest questById = QuestManager.getQuestById(id);
		if (questById == null || questById.isNullQuest()) {
			return null;
		}

		return questById;
	}
}
