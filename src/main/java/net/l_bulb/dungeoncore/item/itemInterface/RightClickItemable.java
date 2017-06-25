package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface RightClickItemable extends ItemInterface {
  /**
   * このアイテムを持った状態で右クリックしたときの処理
   *
   * @param e
   * @return 効果が発動したならTRUE
   */
  public boolean excuteOnRightClick(PlayerInteractEvent e);

  /**
   * アイテムを使った時に消費するならTRUE
   */
  public boolean isConsumeWhenRightClick(PlayerInteractEvent event);
}
