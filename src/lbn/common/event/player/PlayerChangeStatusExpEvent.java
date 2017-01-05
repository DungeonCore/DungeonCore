package lbn.common.event.player;

import lbn.player.status.IStatusManager;
import lbn.player.status.StatusAddReason;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeStatusExpEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	OfflinePlayer p;
	int addExp;
	IStatusManager manager;
	StatusAddReason reason;

	public PlayerChangeStatusExpEvent(OfflinePlayer p, int addExp, IStatusManager manager, StatusAddReason reason) {
		super();
		this.p = p;
		this.addExp = addExp;
		this.manager = manager;
		this.reason = reason;
	}

	public StatusAddReason getReason() {
		return reason;
	}

	public int getAddExp() {
		return addExp;
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
