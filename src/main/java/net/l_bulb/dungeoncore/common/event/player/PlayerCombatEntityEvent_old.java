package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.CustomWeaponItemStack2;

import lombok.Getter;
import lombok.Setter;

/**
 * THELoWで登録されている武器(剣、弓、魔法)による通常攻撃が行われたときに発火します
 */
@Getter
public class PlayerCombatEntityEvent_old extends PlayerEvent {

  private static final HandlerList handlers = new HandlerList();

  LivingEntity enemy;
  @Setter
  double damage;

  CustomWeaponItemStack2 attackItem;

  public PlayerCombatEntityEvent_old(Player who, LivingEntity enemy, ItemStack item, double damage) {
    super(who);
    this.attackItem = CustomWeaponItemStack2.getInstance(item);
    this.damage = damage;
    this.enemy = enemy;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public void callEvent() {
    if (attackItem != null) {
      Bukkit.getServer().getPluginManager().callEvent(this);
    }
  }

}
