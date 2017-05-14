package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.common.event.player.PlayerBreakMagicOreEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;

public interface MagicPickaxeable extends ItemInterface {
  /**
   * 魔法鉱石を壊した時の処理
   *
   * @param e
   */
  public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e);
}
