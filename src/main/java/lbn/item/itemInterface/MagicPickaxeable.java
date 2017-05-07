package lbn.item.itemInterface;

import lbn.common.event.player.PlayerBreakMagicOreEvent;
import lbn.item.ItemInterface;

public interface MagicPickaxeable extends ItemInterface {
  /**
   * 魔法鉱石を壊した時の処理
   * 
   * @param e
   */
  public void onPlayerBreakMagicOreEvent(PlayerBreakMagicOreEvent e);
}
