package lbn.common.event.player;

import lbn.api.TheLowLevelType;
import lbn.api.player.TheLowPlayer;
import lbn.player.status.StatusAddReason;

import org.bukkit.event.HandlerList;

public class PlayerChangeStatusExpEvent extends TheLowPlayerEvent{
	private static final HandlerList handlers = new HandlerList();

	int addExp;
	TheLowLevelType levelType;
	StatusAddReason reason;

	public PlayerChangeStatusExpEvent(TheLowPlayer p, int addExp, TheLowLevelType levelType, StatusAddReason reason) {
		super(p);
		this.addExp = addExp;
		this.levelType = levelType;
		this.reason = reason;
	}

	public StatusAddReason getReason() {
		return reason;
	}

	public int getAddExp() {
		return addExp;
	}

	public TheLowLevelType getLevelType() {
		return levelType;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
