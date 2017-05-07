package lbn.quest;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import lbn.common.event.quest.StartQuestEvent;
import lbn.item.ItemInterface;
import lbn.quest.abstractQuest.QuestType;

public interface Quest {
  public String getId();

  public String getName();

  public String[] getQuestDetail();

  public Set<Quest> getBeforeQuest();

  public boolean isMainQuest();

  public boolean isStartOverlap();

  public boolean canDestory();

  public void onSatisfyComplateCondtion(Player p);

  public String getCurrentInfo(Player p);

  public void playCompleteSound(Player p);

  public void playDistructionSound(Player p);

  public void playStartSound(Player p);

  public boolean isNullQuest();

  public QuestType getQuestType();

  public boolean isComplate(int data);

  /**
   * 受けた後の会話
   * 
   * @return
   */
  public String[] getTalkOnStart();

  /**
   * 完了した後の会話
   * 
   * @return
   */
  public String[] getTalkOnComplate();

  public boolean isShowTitle();

  public Quest getAutoExecuteNextQuest();

  public long getCoolTimeSecound();

  public void giveRewardItem(Player p);

  public boolean canGetRewordItem(Player p);

  public int getAvailableMainLevel();

  public List<String> getRewordText();

  public String getComplateCondition();

  public String getStartVillagerId();

  public String getEndVillagerId();

  public void onStartQuestEvent(StartQuestEvent e);

  /**
   * 進行状況を表示するかどうか
   * 
   * @return
   */
  public boolean isShowProceessText();

  /**
   * クエストを開始したときにもらえるアイテム
   * 
   * @return
   */
  public ItemInterface getQuestBeforeItem();
}
