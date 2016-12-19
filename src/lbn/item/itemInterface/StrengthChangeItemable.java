package lbn.item.itemInterface;

import lbn.common.event.ChangeStrengthLevelItemEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;

public interface StrengthChangeItemable extends Strengthenable{
	void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event);

	void onChangeStrengthLevelItemEvent(ChangeStrengthLevelItemEvent event);

	void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event);
}
