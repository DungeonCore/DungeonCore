package main.item.itemInterface;

import main.common.event.ChangeStrengthLevelItemEvent;
import main.common.event.player.PlayerSetStrengthItemResultEvent;
import main.common.event.player.PlayerStrengthFinishEvent;

public interface StrengthChangeItemable extends Strengthenable{
	void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event);

	void onChangeStrengthLevelItemEvent(ChangeStrengthLevelItemEvent event);

	void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event);
}
