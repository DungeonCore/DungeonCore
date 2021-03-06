package net.l_bulb.dungeoncore.common.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

/**
 * 強化完了時のLoreが変更される瞬間に呼ばれるイベント(失敗か成功かは問わない)
 *
 */
@Getter
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

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
