package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.item.ItemInterface;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public interface BreakBlockItemable extends ItemInterface {
  /**
   * ブロックを壊した時のイベント
   * 
   * @param e
   * @param useItem 壊すときに使ったアイテム
   */
  public void onBlockBreakEvent(BlockBreakEvent e, ItemStack useItem);
}
