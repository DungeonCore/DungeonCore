package net.l_bulb.dungeoncore.quest.abstractQuest;

import java.text.MessageFormat;
import java.util.Set;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.quest.QuestProcessingStatus;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSession;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSessionManager;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashMultimap;

public class PickItemQuest extends AbstractQuest {
  static HashMultimap<String, PickItemQuest> needItemMap = HashMultimap.create();

  public static Set<PickItemQuest> getQuest(ItemInterface item) {
    return needItemMap.get(item.getId());
  }

  private PickItemQuest(String id, String pickItemId, int needCount) {
    super(id);
    this.pickItemId = pickItemId;
    this.needCount = needCount;
    needItemMap.put(pickItemId, this);

  }

  public static PickItemQuest getInstance(String id, String data1, String data2) {
    if (data1 == null) { return null; }
    // 採取数チェック
    int count = JavaUtil.getInt(data2, -1);
    if (count <= 0) { return null; }
    return new PickItemQuest(id, data1, count);
  }

  String pickItemId;

  protected ItemInterface getNeedItem() {
    return ItemManager.getCustomItemById(pickItemId);
  }

  int needCount;

  public int needCount() {
    return needCount;
  }

  /**
   * クエストアイテムを拾ったときの処理
   * 
   * @param e
   * @param session
   * @return もし完了メッセージを出力するならTRUE
   */
  public void onPickUp(PlayerPickupItemEvent e, PlayerQuestSession session) {
    ItemStack pickItem = e.getItem().getItemStack();
    if (pickItem == null) { return; }
    ItemInterface customPickItem = ItemManager.getCustomItem(pickItem);
    if (customPickItem == null) { return; }

    boolean isQuestItem = customPickItem.isQuestItem();

    QuestProcessingStatus processingStatus = session.getProcessingStatus(this);
    // 実行中でない時はクエストアイテムを取得させない
    if (processingStatus != QuestProcessingStatus.PROCESSING) {
      e.setCancelled(true);
      return;
    }

    // 対象のアイテムが存在しないなら何もしない
    ItemInterface needItem = getNeedItem();
    if (needItem == null) { return; }

    int nowCount = session.getQuestData(this);
    int pickCount = pickItem.getAmount();

    // 全て拾ってもクエスト達成しない時
    if (nowCount + pickCount <= needCount()) {
      // クエストアイテムならインベントリにいれない
      if (isQuestItem) {
        e.getItem().remove();
        e.setCancelled(true);
      }
    } else {
      // クエストアイテムなら必要数以上拾わせない
      if (isQuestItem) {
        // ex) 拾った個数3個 - 必要数10個 + 今まで拾った個数8個 = 1 個
        pickItem.setAmount(pickCount - (needCount() - nowCount));
        e.getItem().setItemStack(pickItem);
        e.setCancelled(true);
      }
    }

    // 最終的にDataとして残る個数
    int totalCount = Math.min(nowCount + pickCount, needCount());

    onPickItem(e.getPlayer(), totalCount);
    session.setQuestData(this, totalCount);
  }

  protected void onPickItem(Player player, int nowCount) {
    sendProgressMessage(player, needCount(), nowCount);
    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
  }

  @Override
  public String getCurrentInfo(Player p) {
    int data = PlayerQuestSessionManager.getQuestSession(p).getQuestData(this);
    return "達成度(" + data + "/" + needCount() + ")";
  }

  @Override
  public QuestType getQuestType() {
    return QuestType.PICK_ITEM_QUEST;
  }

  @Override
  public boolean isComplate(int data) {
    return data >= needCount;
  }

  @Override
  public String getComplateCondition() {
    ItemInterface needItem = getNeedItem();
    if (needItem == null) {
      return MessageFormat.format("アイテム[{0}]を{1}個集める", pickItemId, needCount);
    } else {
      return MessageFormat.format("アイテム[{0}]を{1}個集める", needItem.getItemName(), needCount);
    }
  }
}
