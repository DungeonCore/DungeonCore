package net.l_bulb.dungeoncore.quest.abstractQuest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.quest.StartQuestEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestAnnouncement;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.QuestUtil;

public abstract class AbstractQuest implements Quest {
  protected AbstractQuest(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  /**
   * 終了条件を見たした時に行う処理
   */
  @Override
  public void onSatisfyComplateCondtion(Player p) {
    if (endVillager != null) {
      // 最後に報告する村人がいる場合は村人に報告するようにPlayerに伝える
      QuestUtil.sendSatisfyComplateForVillager(endVillager, p);
    } else {
      // 最後に報告する村人がいない場合はこの場所で終了にする
      QuestManager.complateQuest(this, p, true);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (obj instanceof Quest) { return ((Quest) obj).getId().equals(getId()); }
    return false;
  }

  @Override
  public String toString() {
    return getName() + "(" + getId() + ")";
  }

  @Override
  public void playCompleteSound(Player p) {
    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, (float) 0.1);
  }

  @Override
  public void playDistructionSound(Player p) {
    p.playSound(p.getLocation(), Sound.VILLAGER_DEATH, 1, (float) 0.1);
  }

  @Override
  public void playStartSound(Player p) {
    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 0.1);
  }

  boolean canDestory = true;

  @Override
  public boolean canDestory() {
    return !isMainQuest() && canDestory;
  }

  /**
   * クエスト進展メッセージ
   *
   * @param player
   */
  protected void sendProgressMessage(Player player, int needCount, int nowCount) {
    if (isShowProceessText()) {
      QuestAnnouncement.sendQuestProcessInfo(player, MessageFormat.format("{0} ({1}/{2})", getName(), nowCount, needCount));
    }
  }

  String id;

  @Override
  public String getId() {
    return id;
  }

  String name;

  @Override
  public String getName() {
    return name;
  }

  String[] detail;

  @Override
  public String[] getQuestDetail() {
    return detail;
  }

  boolean isSucessBeforeQuest = false;
  String[] beforeQuestIds = null;
  Set<Quest> beforeQuests = null;

  @Override
  public Set<Quest> getBeforeQuest() {
    if (isSucessBeforeQuest) { return beforeQuests; }

    // もし前提クエストがない場合は空を返す
    if (beforeQuestIds == null) {
      beforeQuests = new HashSet<>();
      isSucessBeforeQuest = true;
      return beforeQuests;
    }

    // 前提クエストを空にする
    beforeQuests = new HashSet<>();

    boolean existNullQuest = false;
    // IDからクエストを取得
    for (String id : beforeQuestIds) {
      // クエストが存在しない場合はNullQuestとして処理
      Quest questById = QuestManager.getQuestById(id);
      if (questById == null) {
        existNullQuest = true;
        questById = new NullQuest(id);
      }
      beforeQuests.add(questById);
    }

    // もしnullQuestが存在しない場合はクエスト取得成功にする
    if (!existNullQuest) {
      isSucessBeforeQuest = true;
    }
    return beforeQuests;
  }

  boolean isMain = false;

  @Override
  public boolean isMainQuest() {
    return isMain;
  }

  boolean isStartOverlap = false;

  @Override
  public boolean isStartOverlap() {
    return isStartOverlap;
  }

  String[] talk1 = null;

  @Override
  public String[] getTalkOnStart() {
    return talk1;
  }

  String[] talk2 = null;

  @Override
  public String[] getTalkOnComplate() {
    return talk2;
  }

  Set<String> permiseQuest = null;

  String autoExecuteNextQuestId = null;
  Quest autoExecuteNextQuest = null;

  @Override
  public Quest getAutoExecuteNextQuest() {
    // 次のクエストIDが存在しないならnullを返す
    if (autoExecuteNextQuestId == null) { return null; }

    // 次のクエストIDが見つかっていないなら再検する
    if (autoExecuteNextQuest != null) {
      Quest questIfExist = getQuestIfExist(autoExecuteNextQuestId);
      // 見つからなかったらnullQuestを返す
      if (questIfExist == null || questIfExist.isNullQuest()) { return questIfExist; }
      // 見つかったならsetする
      autoExecuteNextQuest = questIfExist;
    }
    return autoExecuteNextQuest;
  }

  /**
   * クエストがもし存在すればTRUE
   *
   * @param questId
   * @return
   */
  public Quest getQuestIfExist(String questId) {
    if (questId == null) { return null; }
    Quest questById = QuestManager.getQuestById(questId);
    if (questById != null) { return questById; }
    return new NullQuest(questId);
  }

  boolean isShowTitle = true;

  @Override
  public boolean isShowTitle() {
    return isShowTitle;
  }

  long coolTimeSecound = -1;

  @Override
  public long getCoolTimeSecound() {
    return coolTimeSecound;
  }

  @Override
  public boolean canGetRewordItem(Player p) {
    ItemInterface rewordItem = getRewordItem();
    if (rewordItem == null) { return true; }
    ItemStack item = rewordItem.getItem();

    return ItemStackUtil.canGiveItem(p, item);
  }

  @Override
  public void giveRewardItem(Player p) {

    // 報酬アイテムを渡す
    if (getRewordItem() != null) {
      p.getInventory().addItem(getRewordItem().getItem());
    }

    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);

    if (theLowPlayer != null) {
      // お金を渡す
      if (rewordMoney != 0) {
        theLowPlayer.addGalions(rewordMoney, GalionEditReason.quest_reword);
      }

      if (swordExe != 0) {
        theLowPlayer.addExp(LevelType.SWORD, swordExe, StatusAddReason.quest_reword);
      }
      if (bowExe != 0) {
        theLowPlayer.addExp(LevelType.BOW, bowExe, StatusAddReason.quest_reword);
      }
      if (magicExe != 0) {
        theLowPlayer.addExp(LevelType.MAGIC, magicExe, StatusAddReason.quest_reword);
      }
    }
  }

  int swordExe = 0;
  int bowExe = 0;
  int magicExe = 0;
  int rewordMoney = 0;

  String reworldItemId = null;

  public ItemInterface getRewordItem() {
    if (reworldItemId != null) { return ItemManager.getCustomItemById(reworldItemId); }
    return null;
  }

  @Override
  public boolean isNullQuest() {
    return false;
  }

  int availableMainLevel = 0;

  @Override
  public int getAvailableMainLevel() {
    return availableMainLevel;
  }

  @Override
  public List<String> getRewordText() {
    ArrayList<String> texts = new ArrayList<>();
    if (swordExe != 0) {
      texts.add("剣レベル: +" + swordExe + "exp");
    }
    if (bowExe != 0) {
      texts.add("弓レベル: +" + bowExe + "exp");
    }
    if (magicExe != 0) {
      texts.add("魔法レベル: +" + magicExe + "exp");
    }

    if (rewordMoney != 0) {
      texts.add("お金: +" + rewordMoney + "Galions");
    }

    if (getRewordItem() != null) {
      texts.add("アイテム: " + getRewordItem().getItemName());
    }

    if (texts.isEmpty()) {
      texts.add("なし");
    }
    return texts;
  }

  String startVillager = null;

  @Override
  public String getStartVillagerId() {
    return startVillager;
  }

  String endVillager = null;

  @Override
  public String getEndVillagerId() {
    return endVillager;
  }

  @Override
  public void onStartQuestEvent(StartQuestEvent e) {}

  boolean isProcessText = true;

  @Override
  public boolean isShowProceessText() {
    return isProcessText;
  }

  String questBeforeItem = null;

  @Override
  public ItemInterface getQuestBeforeItem() {
    return ItemManager.getCustomItemById(questBeforeItem);
  }

}
