package net.l_bulb.dungeoncore.quest.viewer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.NbtTagConst;
import net.l_bulb.dungeoncore.common.menu.MenuSelector;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.quest.QuestManager.QuestStartStatus;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class QuestConfirmMenu extends MenuSelector {
  static {
    new QuestConfirmMenu().regist();
  }

  private QuestConfirmMenu() {
    super("quest confirm");
  }

  Quest q;

  public QuestConfirmMenu(Quest q) {
    super("quest confirm");
    this.q = q;

    ItemStack reciveItem = ItemStackUtil.getItem(ChatColor.GREEN + "クエストを受注する", Material.WOOL, (byte) 5);
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add(ChatColor.AQUA + "[QUEST] " + q.getName());

    for (String detail : q.getQuestDetail()) {
      arrayList.add(ChatColor.YELLOW + "   " + detail);
    }
    arrayList.add(ChatColor.BLACK + ItemStackUtil.getLoreForIdLine(q.getId()));
    // IDを付与
    ItemStackUtil.setNBTTag(reciveItem, NbtTagConst.THELOW_ITEM_ID, q.getId());

    ItemStackUtil.setLore(reciveItem, arrayList);

    createInventory.setItem(11, reciveItem);
    createInventory.setItem(15, refuseItem);
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    if (item == null || item.getType() != Material.WOOL) { return; }

    // quest idがない時はインベントリを閉じる
    String id = ItemStackUtil.getId(item);
    if (id == null) {
      p.closeInventory();
      return;
    }

    Quest questById = QuestManager.getQuestById(id);
    // クエストを開始する
    QuestStartStatus startQuestStatus = QuestManager.getStartQuestStatus(questById, p);
    if (startQuestStatus.canStart()) {
      QuestManager.startQuest(questById, p, false, startQuestStatus);
    }
    // インベントリを閉める
    p.closeInventory();
  }

  public static ItemStack refuseItem = ItemStackUtil.getItem(ChatColor.RED + "クエストを受注しない", Material.WOOL, (byte) 14);

}
