package net.l_bulb.dungeoncore.quest;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.quest.ComplateQuestEvent;
import net.l_bulb.dungeoncore.common.event.quest.DestructionQuestEvent;
import net.l_bulb.dungeoncore.common.event.quest.QuestEvent;
import net.l_bulb.dungeoncore.common.event.quest.StartQuestEvent;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSession;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSessionManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.Message;
import net.l_bulb.dungeoncore.util.QuestUtil;
import net.l_bulb.dungeoncore.util.TitleSender;

public class QuestManager {
  private static HashMap<String, Quest> allQuestByName = new HashMap<>();

  private static HashMap<String, Quest> allQuestById = new HashMap<>();

  public static void clear() {
    allQuestByName.clear();
    allQuestById.clear();
  }

  public static Quest getQuestByName(String name) {
    return allQuestByName.get(ChatColor.stripColor(name).toUpperCase());
  }

  public static Quest getQuestById(String id) {
    if (id == null) { return null; }
    return allQuestById.get(ChatColor.stripColor(id).toUpperCase());
  }

  public static Set<String> getQuestName() {
    return allQuestByName.keySet();
  }

  public static Set<String> getQuestId() {
    return allQuestById.keySet();
  }

  public static void registQuest(Quest q) {
    if (allQuestById.containsKey(q.getId())) { return; }
    allQuestByName.put(ChatColor.stripColor(q.getName()).toUpperCase(), q);
    allQuestById.put(ChatColor.stripColor(q.getId()).toUpperCase(), q);

    NpcQuestHolder.regist(q);
  }

  public static QuestStartStatus getStartQuestStatus(Quest q, Player p) {
    if (q == null || q.isNullQuest()) { return QuestStartStatus.UNKNOW_QUEST; }

    PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
    QuestProcessingStatus processingStatus = session.getProcessingStatus(q);

    // 現在実行中
    if (processingStatus.isDoing()) {
      // Message.sendMessage(p, Message.QUEST_DOING_NOW, q.getName());
      return QuestStartStatus.DOING_NOW;
    }

    // クエスト数上限
    if (session.getNowQuestSize() >= getMaxQuestCount(p)) {
      // Message.sendMessage(p, Message.QUEST_OVER_COUNT);
      return QuestStartStatus.RECEIVE_COUNT_MAXIMUM;
    }

    // クエストのクールタイム
    long complateDate = session.getComplateDate(q);
    if (complateDate + q.getCoolTimeSecound() * 60 * 1000 >= JavaUtil.getJapanTimeInMillis()) { return QuestStartStatus.REMAIND_COOL_TIME; }

    // メインレベルを取得
    int mainLevel = 0;
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer != null) {
      mainLevel = theLowPlayer.getLevel(LevelType.MAIN);
    }
    // 利用可能レベル
    if (q.getAvailableMainLevel() > mainLevel) { return QuestStartStatus.LACK_AVAILAVLE_MAIN_LEVEL; }

    // 重複不可の時
    if (!q.isStartOverlap()) {
      // 完了回数がでないときは利用不可とする
      if (session.getComplateCount(q) != 0) { return QuestStartStatus.CANNT_OVERLAP; }
    }

    // 前提クエストの確認
    Set<Quest> beforeQuest = q.getBeforeQuest();
    for (Quest quest : beforeQuest) {
      if (!session.isComplate(quest)) {
        // Message.sendMessage(p, "このクエストはまだ受けられません(前提クエスト)");
        return QuestStartStatus.LACK_BEFORE_QUEST;
      }
    }

    return QuestStartStatus.CAN_START;
  }

  /**
   * クエストを開始する
   * 
   * @param q
   * @param p
   * @return
   */
  public static boolean startQuest(Quest q, Player p, boolean force, QuestStartStatus status) {
    return startQuest(q, p, force, status, false);
  }

  /**
   * クエストを開始する
   * 
   * @param q
   * @param p
   * @param force 強制的にクエストを実行
   * @param status
   * @param laterTitle Titleなどを遅れて実行する場合はTRUE
   * @return
   */
  public static boolean startQuest(Quest q, Player p, boolean force, QuestStartStatus status, boolean laterTitle) {
    PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);

    if (!status.canStart()) {
      // 強制実行でないときは実行しない
      if (!force) {
        return false;
      } else {
        // 強制実行だがそれでも実行出来ないときは実行しない
        if (!status.canStartIfForce()) { return false; }
      }
    }

    StartQuestEvent event = new StartQuestEvent(p, q);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) { return false; }

    // クエスト開始時点で渡すアイテムがあるなら渡す
    ItemInterface questBeforeItem = q.getQuestBeforeItem();
    if (questBeforeItem != null) {
      ItemStack questBeforeItemStack = questBeforeItem.getItem();
      // 空きがあるならアイテムを渡す
      if (ItemStackUtil.canGiveItem(p, questBeforeItem.getItem())) {
        p.getInventory().addItem(questBeforeItemStack);
      } else {
        QuestAnnouncement.sendQuestError(p, "インベントリに空きを作ってから開始してください");
        return false;
      }
    }

    // title表示
    if (laterTitle) {
      new BukkitRunnable() {
        @Override
        public void run() {
          showStartEffect(q, p);
        }
      }.runTaskLater(Main.plugin, 20 * 5);
    } else {
      showStartEffect(q, p);
    }

    // 実行中にする
    session.startQuest(q);
    return true;
  }

  /**
   * クエストを開始するためのエフェクトを表示する
   * 
   * @param q
   * @param p
   */
  private static void showStartEffect(Quest q, Player p) {
    if (q.isShowTitle()) {
      // TITLE表示
      TitleSender titleSender = new TitleSender();
      titleSender.setTitle("[Quest] クエスト開始", ChatColor.GOLD, true);
      titleSender.setSubTitle(q.getName(), ChatColor.GOLD, false);
      titleSender.execute(p);
    }

    // 音を鳴らす
    q.playStartSound(p);

    QuestUtil.sendMessageByVillager(p, q.getTalkOnStart());
  }

  private static int getMaxQuestCount(Player p) {
    return 25;
  }

  /**
   * クエストを完了する
   * 
   * @param q
   * @param p
   * @param force 強制的にクエストを終了させる
   */
  public static void complateQuest(Quest q, Player p, boolean force) {
    if (!force && !q.canGetRewordItem(p)) {
      QuestAnnouncement.sendQuestError(p, "インベントリに空きが無いため、報酬を受け取れません。");
      return;
    }

    ComplateQuestEvent event = new ComplateQuestEvent(p, q);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) { return; }

    q.giveRewardItem(p);

    // 報酬を渡す
    PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
    questSession.complateQuest(q);

    if (q.isShowTitle()) {
      TitleSender titleSender = new TitleSender();
      titleSender.setTitle("[Quest] クエスト完了", ChatColor.GOLD, true);
      titleSender.setSubTitle(q.getName(), ChatColor.GOLD, false);
      titleSender.execute(p);
    }
    // 音を鳴らす
    q.playCompleteSound(p);
    // テキストを表示
    QuestUtil.sendMessageByVillager(p, q.getTalkOnComplate());

    // 次のクエストを開始する
    Quest autoExecuteNextQuest = q.getAutoExecuteNextQuest();
    if (autoExecuteNextQuest != null) {
      // 同時に終了と開始のタイトルコマンドを実行できないので開始のタイトルを遅らせて実行する
      boolean laterShow = autoExecuteNextQuest.isShowTitle() && q.isShowTitle();
      startQuest(autoExecuteNextQuest, p, true, getStartQuestStatus(autoExecuteNextQuest, p), laterShow);
    }

  }

  /**
   * クエストを破棄する
   * 
   * @param q
   * @param p
   */
  public static boolean removeQuest(Quest q, Player p) {
    if (q.isMainQuest()) {
      p.sendMessage(ChatColor.RED + "メインクエストは破棄できません。");
      return false;
    }

    if (!q.canDestory()) {
      p.sendMessage(ChatColor.RED + "このクエストは破棄できません。");
      return false;
    }
    QuestEvent event = new DestructionQuestEvent(p, q);
    Bukkit.getServer().getPluginManager().callEvent(event);

    PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
    session.removeQuest(q);

    q.playDistructionSound(p);

    Message.sendMessage(p, Message.QUEST_REMOVE_MESSAGE, q.getName());
    return true;
  }

  // Tellrow使わないかもしれないので一旦コメントアウト
  // static HashMultimap<Quest, Player> canStartQuestByTellrowMap = HashMultimap.create();
  // public static String getStartTellrowCommand(final Player p, final Quest quest) {
  // canStartQuestByTellrowMap.put(quest, p);
  // new BukkitRunnable() {
  // @Override
  // public void run() {
  // canStartQuestByTellrowMap.remove(quest, p);
  // }
  // }.runTaskLater(Main.plugin, 20 * 10);
  // return StringUtils.join(new Object[]{"tellraw ", p.getName(), "
  // {\"text\":\"クエスト受諾(10秒以内にクリック)\",\"bold\":false,\"underlined\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/quest
  // start ", quest.getId(),"\"}}"});
  // }
  // public static boolean canStartQuestByTellrow(Quest q, Player p) {
  // return canStartQuestByTellrowMap.containsEntry(q, p);
  // }

  /**
   * クエストを受けることが出来るかどうかを判断するためにのENUM
   * 
   * @author kensuke
   *
   */
  public static enum QuestStartStatus {
    CAN_START(true, true), UNKNOW_QUEST("クエストが存在しません", false, false), DOING_NOW("同じクエストを同時に受けることはできません", false, false), RECEIVE_COUNT_MAXIMUM(
        "クエスト数が上限に達しました。どれかを破棄してください。", false, true), REMAIND_COOL_TIME("現在このクエストを受注できません(時間制限)", false, true), LACK_AVAILAVLE_MAIN_LEVEL(
        "メインレベルが足りません", false, true), CANNT_OVERLAP("このクエストを再度受けることは出来ません", false, true), LACK_BEFORE_QUEST("前提クエストを完了していません", false, true);

    String errorMessage = null;
    boolean canStart;
    boolean canStartIfForce;

    private QuestStartStatus(boolean canStart, boolean canStartIfForce) {
      this.canStart = canStart;
      this.canStartIfForce = canStartIfForce;
    }

    private QuestStartStatus(String errorMessage, boolean canStart, boolean canStartIfForce) {
      this.canStart = canStart;
      this.errorMessage = errorMessage;
      this.canStartIfForce = canStartIfForce;
    }

    public boolean canStart() {
      return canStart;
    }

    public boolean canStartIfForce() {
      return canStartIfForce;
    }

    public String getCanntMessage() {
      return errorMessage;
    }
  }
}
