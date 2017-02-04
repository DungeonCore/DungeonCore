package lbn.common.event.player;

import lbn.api.TheLowLevelType;
import lbn.api.player.TheLowPlayer;

import org.bukkit.event.HandlerList;

public class PlayerChangeStatusLevelEvent extends TheLowPlayerEvent{
	private static final HandlerList handlers = new HandlerList();

	int level;
	TheLowLevelType type;

	public PlayerChangeStatusLevelEvent(TheLowPlayer player, int level, TheLowLevelType type) {
		super(player);
		this.level = level;
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public int getNowExp() {
		return player.getExp(type);
	}

	public TheLowLevelType getLevelType() {
		return type;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
