package net.l_bulb.dungeoncore.quest.questData;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.QuestSheetRunnable;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.quest.QuestProcessingStatus;
import net.l_bulb.dungeoncore.quest.abstractQuest.QuestType;
import net.l_bulb.dungeoncore.util.JavaUtil;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class PlayerQuestSession implements Serializable {
  private static final long serialVersionUID = 7105868461530126658L;

  // HashMultimap<QuestType, String> doingQuest = HashMultimap.create();
  HashMultimap<QuestType, String> doingQuest = HashMultimap.create();
  HashMap<String, ComplateData> complateQuest = new HashMap<>();

  HashMap<String, Integer> questData = new HashMap<>();

  long lastUpdate = -1;
  {
    lastUpdate = new QuestSheetRunnable(null).getLastUpdate();
  }

  transient OfflinePlayer offlinePlayer;

  public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
    this.offlinePlayer = offlinePlayer;
  }

  public PlayerQuestSession(UUID uniqueId) {
    this.offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
  }

  /**
   * オンラインのPlayerを取得
   * 
   * @return
   */
  public Player getOnlinePlayer() {
    return offlinePlayer.getPlayer();
  }

  public OfflinePlayer getOfflinePlayer() {
    return offlinePlayer;
  }

  /**
   * 指定したクエストを受けていたらTRUE
   * 
   * @param q
   * @return
   */
  public boolean isDoing(Quest q) {
    return doingQuest.containsValue(q.getId());
  }

  public boolean isComplate(Quest q) {
    return complateQuest.containsKey(q.getId());
  }

  public Set<Quest> getDoingQuestListByType(QuestType type) {
    if (lastUpdate < new QuestSheetRunnable(null).getLastUpdate()) {
      updateQuestInstance();
    }

    // IDからクエストを返す
    return getQuestListFromIds(doingQuest.get(type));
  }

  /**
   * IDのリストからクエストリストを取得する
   * 
   * @param set
   * @return
   */
  public HashSet<Quest> getQuestListFromIds(Collection<String> set) {
    // IDをQuestに変換してから返す
    HashSet<Quest> doingQuestList = new HashSet<Quest>();
    for (String questId : set) {
      doingQuestList.add(QuestManager.getQuestById(questId));
    }
    return doingQuestList;
  }

  /**
   * クエストインスタンスを新しいものに更新する
   */
  private void updateQuestInstance() {
    // doingQuestを更新
    // HashMultimap<QuestType, Quest> newDoingQuest = HashMultimap.create();
    // for (Entry<QuestType, Quest> entry : doingQuest.entries()) {
    // Quest questById = QuestManager.getQuestById(entry.getValue().getId());
    // newDoingQuest.put(questById.getQuestType(), questById);
    // }
    // doingQuest = newDoingQuest;
    //
    // //complateQuestを更新
    // HashMap<Quest, ComplateData> newComplateQuest = new HashMap<>();
    // for (Entry<Quest, ComplateData> entry : newComplateQuest.entrySet()) {
    // Quest questById = QuestManager.getQuestById(entry.getKey().getId());
    // newComplateQuest.put(questById, entry.getValue());
    // }
    // complateQuest = newComplateQuest;
    //
    // lastUpdate = new QuestSheetRunnable(null).getLastUpdate();
  }

  public Collection<Quest> getDoingQuestList() {
    if (lastUpdate < new QuestSheetRunnable(null).getLastUpdate()) {
      updateQuestInstance();
    }
    return getQuestListFromIds(doingQuest.values());
  }

  public Collection<Quest> getComplateQuestList() {
    if (lastUpdate < new QuestSheetRunnable(null).getLastUpdate()) {
      updateQuestInstance();
    }
    return getQuestListFromIds(doingQuest.values());
  }

  /**
   * クエストのデータ値を渡す
   * 
   * @param q
   * @return
   */
  public int getQuestData(Quest q) {
    if (questData.containsKey(q.getId())) { return questData.get(q.getId()); }
    return 0;
  }

  public void setQuestData(Quest q, int data) {
    questData.put(q.getId(), data);
  }

  public long getComplateDate(Quest q) {
    if (complateQuest.containsKey(q.getId())) { return complateQuest.get(q.getId()).complateData; }
    return 0;
  }

  public long getComplateCount(Quest q) {
    if (complateQuest.containsKey(q.getId())) { return complateQuest.get(q.getId()).complateCount; }
    return 0;
  }

  public void startQuest(Quest q) {
    doingQuest.put(q.getQuestType(), q.getId());
  }

  public void removeQuest(Quest q) {
    doingQuest.remove(q.getQuestType(), q.getId());
    questData.remove(q.getId());
  }

  public void complateQuest(Quest q) {
    ComplateData complateData = complateQuest.get(q.getId());
    if (complateData == null) {
      complateData = new ComplateData();
    }
    // 完了時間をセット
    complateData.complateCount = complateData.complateCount + 1;

    // 完了時間をセット
    complateData.complateData = JavaUtil.getJapanTimeInMillis();
    complateQuest.put(q.getId(), complateData);

    // 実行中から削除
    doingQuest.remove(q.getQuestType(), q.getId());
    questData.remove(q.getId());
  }

  public int getNowQuestSize() {
    return doingQuest.values().size();
  }

  public QuestProcessingStatus getProcessingStatus(Quest q) {
    // クエストを実行しているか確認
    if (!isDoing(q)) {
      // 実行していなければNOT_START
      return QuestProcessingStatus.NOT_START;
    }

    // 終了条件を満たしていないならPROCESSING
    if (!q.isComplate(getQuestData(q))) { return QuestProcessingStatus.PROCESSING; }
    return QuestProcessingStatus.PROCESS_END;
  }
}

class ComplateData {
  long complateData = 0;
  int complateCount = 0;
}
