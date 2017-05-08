package net.l_bulb.dungeoncore.quest.abstractQuest;

import java.text.MessageFormat;
import java.util.HashSet;

import net.l_bulb.dungeoncore.common.event.quest.StartQuestEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpcs;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestAnnouncement;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSession;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TakeItemQuest extends AbstractQuest {
  static HashSet<TakeItemQuest> takeItemQuestMap = new HashSet<TakeItemQuest>();

  protected TakeItemQuest(String id, String itemId, int count) {
    super(id);
    this.itemId = itemId;
    this.count = count;
    takeItemQuestMap.add(this);
  }

  /**
   * 指定されたクエストからTakeItemQuestならTrue
   * 
   * @param quest
   * @return
   */
  public static boolean isTakeItemQuest(Quest quest) {
    return takeItemQuestMap.contains(quest);
  }

  String itemId;
  int count;

  public void onTouchVillager(Player p, Entity entity, PlayerQuestSession session) {
    if (entity == null) { return; }
    // NPCが同じかチェック
    String name = entity.getCustomName();
    if (!name.equalsIgnoreCase(getEndVillagerId())) { return; }

    ItemStack item = getNeedItem().getItem();
    item.setAmount(count);
    // アイテムを持っているか確認
    PlayerInventory inventory = p.getInventory();
    if (!inventory.contains(item)) { return; }
    // アイテムを削除する
    inventory.remove(getNeedItem().getItem());
    session.setQuestData(this, 1);
  }

  @Override
  public QuestType getQuestType() {
    return QuestType.TAKE_ITEM_QUEST;
  }

  protected ItemInterface getNeedItem() {
    return ItemManager.getCustomItemById(itemId);
  }

  @Override
  public String getComplateCondition() {
    ItemInterface needItem = getNeedItem();

    String villagerName = VillagerNpcs.getVillagerName(getEndVillagerId());

    if (needItem == null) {
      return MessageFormat.format("{0}のところへアイテム[{1}]を持っていく", villagerName, itemId);
    } else {
      return MessageFormat.format("{0}のところへアイテム[{1}]を持っていく", villagerName, needItem.getItemName());
    }
  }

  public static TakeItemQuest getInstance(String id, String data1, String data2) {
    return new TakeItemQuest(id, data1, JavaUtil.getInt(data2, 1));
  }

  @Override
  public void onStartQuestEvent(StartQuestEvent e) {
    super.onStartQuestEvent(e);
    ItemInterface needItem = getNeedItem();
    // アイテムが存在しなければ無視する
    if (needItem == null) {
      QuestAnnouncement.sendQuestError(e.getPlayer(), "現在このクエストを受けることが出来ません");
      e.setCancelled(true);
      return;
    }

    if (!ItemStackUtil.canGiveItem(e.getPlayer(), needItem.getItem())) {
      QuestAnnouncement.sendQuestError(e.getPlayer(), "インベントがいっぱいです。空きを作ってください。");
      e.setCancelled(true);
      return;
    }
    e.getPlayer().getInventory().addItem(needItem.getItem());
  }

  @Override
  public boolean isComplate(int data) {
    return data == 1;
  }

  @Override
  public String getCurrentInfo(Player p) {
    return "達成度(0/1)";
  }
}
