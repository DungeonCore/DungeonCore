package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;

import lombok.Getter;

@Getter
public class PlayerKillEntityEvent extends PlayerEvent {
  private static final HandlerList handlers = new HandlerList();

  LivingEntity enemy;

  private ItemStack item;

  public PlayerKillEntityEvent(Player who, LivingEntity enemy, ItemStack item) {
    super(who);
    this.enemy = enemy;
    this.item = item;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public ItemInterface getItemInterface() {
    return ItemManager.getCustomItem(item);
  }

  public void callEvent() {
    Bukkit.getServer().getPluginManager().callEvent(this);
  }
}
