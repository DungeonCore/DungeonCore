package lbn.common.event.player;

import lbn.player.status.IStatusManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeStatusLevelEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	OfflinePlayer p;
	int oldlevel;
	int nowLevel;
	IStatusManager manager;

	public PlayerChangeStatusLevelEvent(OfflinePlayer p, int oldlevel, int nowLevel, IStatusManager manager) {
		this.p = p;
		this.oldlevel = oldlevel;
		this.nowLevel = nowLevel;
		this.manager = manager;
	}

	public int getNowLevel() {
		return nowLevel;
	}

	public int getNowExp() {
		return manager.getExp(p);
	}

	public int getBeforeLevel() {
		return oldlevel;
	}

	public IStatusManager getManager() {
		return manager;
	}

	public OfflinePlayer getPlayer() {
		return p;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
