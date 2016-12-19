package main.player.status;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public interface IStatusManager {

	public String getManagerName();

	/**
	 * 現在のExpを取得
	 * @param p
	 * @return
	 */
	public int getExp(OfflinePlayer p);

	/**
	 * 現在のlevelを取得
	 * @param p
	 * @return
	 */
	public int getLevel(OfflinePlayer p);

	public int getMaxLevel();

	public void addExp(OfflinePlayer target, int value, StatusAddReason reason);

	public void setLevel(OfflinePlayer target, int value);

	PlayerStatus getStatus(OfflinePlayer target);

	IStatusDetail getDetail();

	int getViewRowSize();

	/**
	 * levelからexpを取得
	 * @param level
	 * @return
	 */
	public int getExpByLevel(int nextLevel, int[] otherLevelList);

	public ItemStack getLevelViewIcon(int viewIndex, OfflinePlayer p, PlayerStatus status);
}
