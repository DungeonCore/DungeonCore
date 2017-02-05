package lbn.api.player;

import java.util.List;
import java.util.UUID;

import lbn.api.AbilityType;
import lbn.api.LevelType;
import lbn.api.PlayerStatusType;
import lbn.common.other.DungeonData;
import lbn.money.GalionEditReason;
import lbn.player.status.StatusAddReason;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface TheLowPlayer {
	/**
	 * The Lowのレベルを取得
	 * @param type
	 * @return
	 */
	public int getLevel(LevelType type);

	/**
	 * The Lowのレベルを取得
	 * @param type
	 * @return
	 */
	public int getExp(LevelType type);

	/**
	 * The Lowのレベルをセットする
	 * @param type
	 * @param level
	 */
	public void setLevel(LevelType type, int level);

	/**
	 * The Lowの経験値を追加する
	 * @param type
	 * @return
	 */
	public void addExp(LevelType type, int exp, StatusAddReason reason);

	/**
	 * 最大レベルを取得する
	 * @param type
	 * @return
	 */
	public int getMaxLevel(LevelType type);

	/**
	 * 最大レベルをセットする
	 * @param type
	 * @return
	 */
	public void setMaxLevel(LevelType type, int value);

	/**
	 * 次にレベルになるまでに必要な総経験値を取得
	 * @param type
	 * @param level
	 * @return
	 */
	public int getNeedExp(LevelType type, int level);

	/**
	 *お金を取得
	 * @return
	 */
	public int getGalions();

	/**
	 *お金を与える
	 * @return
	 */
	public void addGalions(int galions, GalionEditReason reason);

	/**
	 *お金をセットする
	 * @return
	 */
	public void setGalions(int galions, GalionEditReason reason);

	/**
	 * Offline Playerを取得
	 * @return
	 */
	public OfflinePlayer getOfflinePlayer();

	/**
	 * Online Playerを取得
	 * @return
	 */
	public Player getOnlinePlayer();

	/**
	 * OnlineならTRUE
	 * @return
	 */
	public boolean isOnline();

	/**
	 * 今いるダンジョンを取得
	 * @return
	 */
	public DungeonData getInDungeonId();

	/**
	 * 今いるダンジョンのIDをセットする
	 * @return
	 */
	public void setInDungeonId(DungeonData dungeon);

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
	 * @return
	 */
	public String getSaveType();

	/**
	 * 最後に死んだ時間
	 * @return
	 */
	public Long getLastDeathTimeMillis();

	/**
	 * 最後に死んだ時間をセット
	 * @return
	 */
	public void setLastDeathTimeMillis(long time);

	/**
	 * Abilityを追加
	 * @param ablity
	 */
	public void addAbility(AbilityInterface ablity);

	/**
	 * Abilityを削除
	 * @param ablity
	 */
	public void removeAbility(AbilityInterface ablity);

	/**
	 * Abilityデータを適応させる
	 * @param check TRUE：Abilityの整合性チェックを行ってから適応, FALSE：整合性チェックを行わないで適応
	 */
	public void applyAbilityData(boolean check);

	/**
	 * Playerの追加ステータス情報を取得する
	 * @param type
	 * @return
	 */
	public double getStatusData(PlayerStatusType type);

	/**
	 * 転生を行う
	 * @param reincarnationInterface 転生効果
	 * @param levelType 転生を行うLevelType
	 * @return 転生に成功したらTRUE
	 */
	public boolean doReincarnation(ReincarnationInterface reincarnationInterface, LevelType levelType);

	/**
	 * 転生できるならTRUE
	 * @param levelType 転生を行うLevelType
	 * @return
	 */
	public boolean canReincarnation(LevelType levelType);

	/**
	 * 指定されたLevelTypeで何回転生をしたか取得
	 * @param levelType
	 * @return
	 */
	public int getEachReincarnationCount(LevelType levelType);

	/**
	 * 全ての転生データを取得
	 * @return
	 */
	public List<OneReincarnationData> getReincarnationData();

	/**
	 * 指定したAbilityTypeのAbilityデータを削除する
	 * @param abilityType
	 */
	void clearAbilityData(AbilityType abilityType);
}
