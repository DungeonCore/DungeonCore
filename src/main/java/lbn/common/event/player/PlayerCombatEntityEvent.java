package lbn.common.event.player;

import lbn.item.customItem.attackitem.AttackItemStack;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * THELoWで登録されている武器(剣、弓、魔法)による通常攻撃が行われたときに発火します
 */
public class PlayerCombatEntityEvent extends PlayerEvent {

  private static final HandlerList handlers = new HandlerList();

  LivingEntity entity;
  double damage;

  AttackItemStack attackItem;

  public PlayerCombatEntityEvent(Player who, LivingEntity entity, ItemStack item, double damage) {
    super(who);
    this.attackItem = AttackItemStack.getInstance(item);
    this.damage = damage;
    this.entity = entity;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public double getDamage() {
    return damage;
  }

  public void setDamage(double damage) {
    this.damage = damage;
  }

  public AttackItemStack getAttackItem() {
    return attackItem;
  }

  public LivingEntity getEnemy() {
    return entity;
  }

  public void callEvent() {
    if (attackItem != null) {
      Bukkit.getServer().getPluginManager().callEvent(this);
    }
  }

}
