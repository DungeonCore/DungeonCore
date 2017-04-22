package lbn.common.event.player;

import lbn.api.LevelType;
import lbn.api.player.OneReincarnationData;
import lbn.api.player.TheLowPlayer;

import org.bukkit.event.HandlerList;

public class PlayerCompleteReincarnationEvent extends TheLowPlayerEvent {

	OneReincarnationData data;

	/**
	 * 転生が完了した時のEvent
	 * 
	 * @param player
	 * @param data
	 */
	public PlayerCompleteReincarnationEvent(TheLowPlayer player, OneReincarnationData data) {
		super(player);
		this.data = data;
	}

	/**
	 * 今回の転生の転生データを取得する
	 * 
	 * @return
	 */
	public OneReincarnationData getReincarnationData() {
		return data;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * どのLevelTypeを転生したか
	 * 
	 * @return
	 */
	public LevelType getLevelType() {
		return data.getLevelType();
	}

}
