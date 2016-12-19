package lbn.player.status;

import org.bukkit.OfflinePlayer;


public class PlayerStatus {
	OfflinePlayer p;
	IStatusManager manager;

	int nowLevel = -1;
	int nowExp = -1;

	public PlayerStatus(OfflinePlayer p, IStatusManager manager2) {
		this.p = p;
		this.manager = manager2;
		nowLevel = getManager().getLevel(p);
		nowExp = getManager().getExp(p);
	}

	public int getLevel() {
		return nowLevel;
	}

	public int getExp() {
		return nowExp;
	}

	public int getRemainExp() {
		if (nowLevel == getManager().getMaxLevel()) {
			return 0;
		}
		int nextLevel = nowLevel + 1;
		if (manager instanceof AbstractNormalStatusManager) {
			return getManager().getExpByLevel(nextLevel, ((AbstractNormalStatusManager)manager).getOtherLevelList(p)) - nowExp;
		} else {
			return getManager().getExpByLevel(nextLevel, null) - nowExp;
		}

	}

	public OfflinePlayer getPlayer() {
		return p;
	}

	public IStatusManager getManager() {
		return manager;
	}
}
