package lbn.api.player;

import java.util.UUID;

import lbn.api.PlayerStatusType;
import lbn.api.TheLowLevelType;
import lbn.common.other.DungeonData;
import lbn.money.GalionEditReason;
import lbn.player.ability.AbilityInterface;
import lbn.player.status.StatusAddReason;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface TheLowPlayer {
	/**
	 * The Lowのレベルを取得
	 * @param type
	 * @return
	 */
	public int getLevel(TheLowLevelType type);

	/**
	 * The Lowのレベルを取得
	 * @param type
	 * @return
	 */
	public int getExp(TheLowLevelType type);

	/**
	 * The Lowのレベルをセットする
	 * @param type
	 * @param level
	 */
	public void setLevel(TheLowLevelType type, int level);

	/**
	 * The Lowの経験値を追加する
	 * @param type
	 * @return
	 */
	public void addExp(TheLowLevelType type, int exp, StatusAddReason reason);

	/**
	 * 最大レベルを取得する
	 * @param type
	 * @return
	 */
	public int getMaxLevel(TheLowLevelType type);

	/**
	 * 最大レベルをセットする
	 * @param type
	 * @return
	 */
	public void setMaxLevel(TheLowLevelType type, int value);

	/**
	 * 次にレベルになるまでに必要な総経験値を取得
	 * @param type
	 * @param level
	 * @return
	 */
	public int getNeedExp(TheLowLevelType type, int level);

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
	 * Playerの追加ステータス情報を取得する
	 * @param type
	 * @return
	 */
	public double getStatusData(PlayerStatusType type);
}
