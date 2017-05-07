package lbn.item.itemInterface;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;

public interface BreakBlockItemable extends ItemInterface {
  /**
   * ブロックを壊した時のイベント
   * 
   * @param e
   * @param useItem 壊すときに使ったアイテム
   */
  public void onBlockBreakEvent(BlockBreakEvent e, ItemStack useItem);
}
