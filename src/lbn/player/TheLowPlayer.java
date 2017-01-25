package lbn.player;

import java.util.UUID;

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
	public int getTheLowLevel(TheLowLevelType type);

	/**
	 * The Lowのレベルを取得
	 * @param type
	 * @return
	 */
	public int getTheLowExp(TheLowLevelType type);

	/**
	 * The Lowのレベルをセットする
	 * @param type
	 * @param level
	 */
	public void setTheLowLevel(TheLowLevelType type, int level);

	/**
	 * The Lowの経験値を追加する
	 * @param type
	 * @return
	 */
	public void addTheLowExp(TheLowLevelType type, int exp, StatusAddReason reason);

	public int getMaxLevel(TheLowLevelType type);

	public int getNeedExp(TheLowLevelType type, int level);

	/**
	 * 最大MagiPointレベル
	 * @return
	 */
	public int getMaxMagicPoint();

	/**
	 * 最大MagiPointレベル
	 * @return
	 */
	public void setMaxMagicPoint(int magipointLevel);

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
}
