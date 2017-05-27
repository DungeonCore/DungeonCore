package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.player.ItemType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCombatEntityEvent extends Event {

  /**
   * @param attacker 攻撃を与えたEntity
   * @param damage ダメージ
   * @param customItem 攻撃に使用したカスタムアイテム
   * @param itemStack 攻撃に使用したアイテム
   * @param target 攻撃対象
   * @param isNormalAttack 通常攻撃ならTRUE
   */
  public PlayerCombatEntityEvent(LivingEntity attacker, double damage, AbstractAttackItem customItem, ItemStack itemStack, boolean isNormalAttack,
      Entity target) {
    this.attacker = attacker;
    this.damage = damage;
    this.customItem = customItem;
    this.itemStack = itemStack;
    this.isNormalAttack = isNormalAttack;
    this.target = target;
  }

  // 攻撃者
  LivingEntity attacker;

  // 与えたダメージ
  double damage;

  // 使用したスタムアイテム
  AbstractAttackItem customItem;

  // 使用したアイテムスタック
  ItemStack itemStack;

  // 通常攻撃ならTRUE
  boolean isNormalAttack;

  // 攻撃を受けたEntity
  Entity target;

  /**
   * アイテムタイプを取得
   */
  public ItemType geItemType() {
    if (customItem == null) { return null; }
    return customItem.getAttackType();
  }

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return null;
  }

  public PlayerCombatEntityEvent callEvent() {
    Bukkit.getServer().getPluginManager().callEvent(this);
    return this;
  }

}
