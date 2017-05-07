package lbn.common.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 強化完了時のLoreが変更される瞬間に呼ばれるイベント(失敗か成功かは問わない)
 *
 */
public class ChangeStrengthLevelItemEvent extends Event {

  ItemStack before;
  ItemStack after;
  int nextLevel;
  int beforeLevel;

  public ChangeStrengthLevelItemEvent(ItemStack before, ItemStack after, int beforeLevel, int nextLevel) {
    this.before = before;
    this.after = after;
    this.nextLevel = nextLevel;
    this.beforeLevel = beforeLevel;
  }

  public ItemStack getBefore() {
    return before;
  }

  public int getBeforeLevel() {
    return beforeLevel;
  }

  public ItemStack getAfter() {
    return after;
  }

  public int getNextLevel() {
    return nextLevel;
  }

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
