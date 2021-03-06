package net.l_bulb.dungeoncore.quest.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.QuestSheetRunnable;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class QuestSelectViewIcon {
  private final static String ID_PREFIX = ChatColor.BLACK + "q_view_id:";

  private static long lastUpdate = -1;

  public static boolean isThisItem(ItemStack item) {
    List<String> lore = ItemStackUtil.getLore(item);
    for (String string : lore) {
      if (string.contains(ID_PREFIX)) { return true; }
    }
    return false;
  }

  static HashMap<Quest, ItemStack> itemCache = new HashMap<>();

  public static ItemStack getItemStack(Quest q) {
    if (q == null || q.isNullQuest()) { return null; }

    // キャッシュ登録を始めるよりもあとにスプレットシートを登録したらキャッシュをを削除する
    long sheetLastUpdate = new QuestSheetRunnable(null).getLastUpdate();
    if (lastUpdate < sheetLastUpdate) {
      itemCache.clear();
      lastUpdate = sheetLastUpdate;
    }

    // キャッシュが残っていたらそれを使う
    if (itemCache.containsKey(q)) { return itemCache.get(q); }

    // Itemを作成
    ArrayList<String> lore = new ArrayList<>();
    for (String detail : q.getQuestDetail()) {
      lore.add(ChatColor.AQUA + detail);
    }
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
    if (!isThisItem(item)) { return null; }

    List<String> lore = ItemStackUtil.getLore(item);

    String id = null;
    for (String string : lore) {
      if (string.contains(ID_PREFIX)) {
        id = string.replace(ID_PREFIX, "").trim();
      }
    }

    Quest questById = QuestManager.getQuestById(id);
    if (questById == null || questById.isNullQuest()) { return null; }

    return questById;
  }
}
