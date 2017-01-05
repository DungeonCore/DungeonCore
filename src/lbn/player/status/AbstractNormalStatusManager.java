package lbn.player.status;

import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;


public abstract class AbstractNormalStatusManager extends AbstractStatusManager{
	abstract protected AbstractNormalStatusManager[] getOtherMamager();

	@Override
	public PlayerStatus getStatus(OfflinePlayer target) {
		return new PlayerStatus(target, this);
	}

	@Override
	public void addExp(OfflinePlayer p, int exp, StatusAddReason reason) {
		//加算前のレベルを取得
		int beforeLevel = getLevel(p);

		if (beforeLevel >= getMaxLevel()) {
			return;
		}

		//現在の経験値を取得
		int integer = getExp(p);

		PlayerChangeStatusExpEvent event1 = new PlayerChangeStatusExpEvent(p, exp, this, reason);
		Bukkit.getServer().getPluginManager().callEvent(event1);
		exp = event1.getAddExp();

		int newExp = integer + exp;

		int nextLevel = beforeLevel;
		while (true) {
			int nextNeedExp = getExpByLevel(nextLevel + 1, getOtherLevelList(p));
			if (nextNeedExp > newExp) {
				break;
			}
			newExp -= nextNeedExp;
			nextLevel++;
		}

		getExpMap().put(p.getUniqueId(), newExp);

		nextLevel = Math.min(getMaxLevel(), nextLevel);

		if (beforeLevel != nextLevel) {
			PlayerChangeStatusLevelEvent event = new PlayerChangeStatusLevelEvent(p, beforeLevel, nextLevel, this);
			Bukkit.getServer().getPluginManager().callEvent(event);
			levelup(p, nextLevel);
		}

	}

//	public static int getLevelByExpq(int exp, int[] otherLevelList) {
//		return (int) Math.floor(Math.pow(((exp - 0.8 * Math.pow(otherLevelList[0] + otherLevelList[1], 1.6)) / 1.3), 2.0/3.0));
//	}

	@Override
	public int getExpByLevel(int nextLevel, int[] otherLevelList) {
		return (int) Math.ceil(180 + 1.3 * Math.pow(nextLevel, 2.5) + 0.8 * Math.pow(otherLevelList[0] + otherLevelList[1], 1.1));
	}

//	public static int getExpByLevela(int nextLevel, int[] otherLevelList) {
//		return (int) Math.ceil(80 + 1.3 * Math.pow(nextLevel, 1.5) + 0.8 * Math.pow(otherLevelList[0] + otherLevelList[1], 1.6));
//	}

	public int[] getOtherLevelList(OfflinePlayer p) {
		AbstractNormalStatusManager[] otherMamager = getOtherMamager();
		int[] otherLevelList = new int[otherMamager.length];
		for (int i = 0; i < otherLevelList.length; i++) {
			otherLevelList[i] = otherMamager[i].getLevel(p);
		}
		return otherLevelList;
	}


}

