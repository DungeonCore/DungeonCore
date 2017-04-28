package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 強化完成形のアイテムがRESULT欄に表示される瞬間
 *
 */
public class PlayerSetStrengthItemResultEvent extends PlayerEvent {

  ItemStack item;

  int nextLevel;

  private boolean isSuccess;

  public PlayerSetStrengthItemResultEvent(Player who, ItemStack item, int nextLevel, boolean isSuccess) {
    super(who);
    this.item = item;
    this.nextLevel = nextLevel;
    this.isSuccess = isSuccess;
  }

  public ItemStack getItem() {
    return item;
  }

  public void setItem(ItemStack item) {
    this.item = item;
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

  public boolean isSuccess() {
    return isSuccess;
  }
}
