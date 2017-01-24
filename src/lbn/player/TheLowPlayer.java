package lbn.player;

import lbn.common.other.DungeonData;

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
	 * @return
	 */
	public void setTheLowLevel(TheLowLevelType type, int level);

	/**
	 * The Lowの経験値をセットする
	 * @param type
	 * @return
	 */
	public void setTheLowExp(TheLowLevelType type, int exp);

	/**
	 *お金を取得
	 * @return
	 */
	public int getGalions();

	/**
	 *お金をセットする
	 * @return
	 */
	public void setGalions(int galions);

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
	 * 今いるダンジョンを取得
	 * @return
	 */
	public DungeonData getInDungeonId();

	/**
	 * 今いるダンジョンのIDをセットする
	 * @return
	 */
	public void setInDungeonId(DungeonData dungeon);
}
