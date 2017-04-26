package lbn.quest.viewer;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import lbn.common.menu.MenuSelector;
import lbn.common.menu.SelectRunnable;
import lbn.quest.Quest;
import lbn.util.Message;

public class QuestMenuSelector extends MenuSelector {
  public static QuestMenuSelectorRunnable run = new QuestMenuSelectorRunnable();

  static {
    new QuestMenuSelector().regist();
  }

  List<Quest> canStartQuestList;

  public QuestMenuSelector(List<Quest> canStartQuestList) {
    super("Quest Selector");
    this.canStartQuestList = canStartQuestList;
  }

  private QuestMenuSelector() {
    super("Quest Selector");
  }

  @Override
  public void open(Player p) {
    for (Quest quest : canStartQuestList) {
      createInventory.addItem(QuestSelectViewIcon.getItemStack(quest));
    }
    p.openInventory(createInventory);
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    run.run(p, item);
  }
}

class QuestMenuSelectorRunnable implements SelectRunnable {
  static {
    MenuSelector menuSelecor = new MenuSelector("quest_confirm");
    menuSelecor.regist();
    new MenuSelector("quest_confirm").regist();
  }

  @Override
  public void run(Player p, ItemStack item) {
    // クエストアイテムでないとき
    if (!QuestSelectViewIcon.isThisItem(item)) {
      // Message.sendMessage(p, ChatColor.RED + "エラーが発生しました。このクエストを開始できません。(1)");
      // p.closeInventory();
      // 何もしない
      return;
    }

    // クエストが存在しないとき
    Quest questByItem = QuestSelectViewIcon.getQuestByItem(item);
    if (questByItem == null) {
      Message.sendMessage(p, ChatColor.RED + "エラーが発生しました。このクエストを開始できません。(2)");
      p.closeInventory();
      return;
    }

    MenuSelector menuSelecor = new QuestConfirmMenu(questByItem);
    menuSelecor.open(p);
  }
}
