package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

/**
 * 強化完成形のアイテムがRESULT欄に表示される瞬間
 *
 */
@Getter
public class PlayerSetStrengthItemResultEvent extends PlayerEvent {
  @Setter
  ItemStack item;

  int nextLevel;

  private boolean isSuccess;

  public PlayerSetStrengthItemResultEvent(Player who, ItemStack item, int nextLevel, boolean isSuccess) {
    super(who);
    this.item = item;
    this.nextLevel = nextLevel;
    this.isSuccess = isSuccess;
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
