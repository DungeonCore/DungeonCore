package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;

public interface StrengthChangeItemable extends Strengthenable {
  /**
   * 強化完成形のアイテムがRESULT欄に表示される瞬間
   *
   * @param event
   */
  void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event);

  /**
   * このアイテムの強化を完全に完了させた瞬間
   *
   * @param event
   */
  void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event);
}
