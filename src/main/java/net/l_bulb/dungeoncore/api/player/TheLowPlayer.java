package net.l_bulb.dungeoncore.api.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonData;
import net.l_bulb.dungeoncore.common.place.dungeon.DungeonDataOld;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;

public interface TheLowPlayer {
  /**
   * The Lowのレベルを取得
   * 
   * @param type
   * @return
   */
  public int getLevel(LevelType type);

  /**
   * The Lowのレベルを取得
   * 
   * @param type
   * @return
   */
  public int getExp(LevelType type);

  /**
   * The Lowのレベルをセットする
   * 
   * @param type
   * @param level
   */
  public void setLevel(LevelType type, int level);

  /**
   * The Lowの経験値を追加する
   * 
   * @param type
   * @return
   */
  public void addExp(LevelType type, int exp, StatusAddReason reason);

  /**
   * 最大レベルを取得する
   * 
   * @param type
   * @return
   */
  public int getMaxLevel(LevelType type);

  /**
   * 最大レベルをセットする
   * 
   * @param type
   * @return
   */
  public void setMaxLevel(LevelType type, int value);

  /**
   * 次にレベルになるまでに必要な総経験値を取得
   * 
   * @param type
   * @param level
   * @return
   */
  public int getNeedExp(LevelType type, int level);

  /**
   * お金を取得
   * 
   * @return
   */
  public int getGalions();

  /**
   * お金を与える
   * 
   * @return
   */
  public void addGalions(int galions, GalionEditReason reason);

  /**
   * お金をセットする
   * 
   * @return
   */
  public void setGalions(int galions, GalionEditReason reason);

  /**
   * Offline Playerを取得
   * 
   * @return
   */
  public OfflinePlayer getOfflinePlayer();

  /**
   * Online Playerを取得
   * 
   * @return
   */
  public Player getOnlinePlayer();

  /**
   * OnlineならTRUE
   * 
   * @return
   */
  public boolean isOnline();

  /**
   * 今いるダンジョンを取得
   * 
   * @return
   */
  public DungeonData getInDungeonId();

  /**
   * 今いるダンジョンのIDをセットする
   * 
   * @return
   */
  public void setInDungeonId(DungeonDataOld dungeon);

  /**
   * Player名を取得
   */
  public String getName();

  /**
   * PlayerのUUIDを取得
   */
  public UUID getUUID();

  /**
   * 現在のSaveTypeを取得
   * 
   * @return
   */
  public String getSaveType();

  /**
   * 最後に死んだ時間
   * 
   * @return
   */
  public Long getLastDeathTimeMillis();

  /**
   * 最後に死んだ時間をセット
   * 
   * @return
   */
  public void setLastDeathTimeMillis(long time);

  /**
   * Abilityを追加
   * 
   * @param ablity
   */
  public void addAbility(AbilityInterface ablity);

  /**
   * Abilityを削除
   * 
   * @param ablity
   */
  public void removeAbility(AbilityInterface ablity);

  /**
   * データの整合性をチェックする
   * 
   * @param level LEVEL1:チェックを行わないでPlayerステータスの適応を行う, LEVEL2:チェックを行いPlayerステータスの変更を行う
   */
  public void fixIntegrity(CheckIntegrityLevel level);

  /**
   * Playerの追加ステータス情報を取得する
   * 
   * @param type
   * @return
   */
  public double getStatusData(PlayerStatusType type);

  /**
   * 転生を行う
   * 
   * @param reincarnationInterface 転生効果
   * @param levelType 転生を行うLevelType
   * @return 転生に成功したらTRUE
   */
  public boolean doReincarnation(ReincarnationInterface reincarnationInterface, LevelType levelType);

  /**
   * 転生できるならTRUE
   * 
   * @param levelType 転生を行うLevelType
   * @return
   */
  public boolean canReincarnation(LevelType levelType);

  /**
   * 同じPlayerならTRUE
   * 
   * @param p
   * @return
   */
  public boolean equalsPlayer(Player p);

  /**
   * 指定されたLevelTypeで何回転生をしたか取得
   * 
   * @param levelType
   * @return
   */
  public int getEachReincarnationCount(LevelType levelType);

  public static enum CheckIntegrityLevel {
    LEVEL1, // チェックを行わないでPlayerステータスの変更を行う
    LEVEL2,// チェックを行い、Playerステータスの変更を行う
  }
}
