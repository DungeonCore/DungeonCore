package net.l_bulb.dungeoncore.common.dropingEntity;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.CustomWeaponItemStack2;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.player.ItemType;

import lombok.Getter;
import lombok.Setter;

/**
 * CustomItemでダメージを与えた時のEvent
 *
 */
@Getter
@Setter
public class CombatEntityEvent extends Event {

  /**
   * @param attacker 攻撃を与えたEntity
   * @param damage ダメージ
   * @param customItem 攻撃に使用したカスタムアイテム
   * @param itemStack 攻撃に使用したアイテム
   * @param target 攻撃対象
   * @param isNormalAttack 通常攻撃ならTRUE
   */
  public CombatEntityEvent(LivingEntity attacker, double damage, AbstractAttackItem customItem, ItemStack itemStack, boolean isNormalAttack,
      LivingEntity target) {
    this.attacker = attacker;
    this.customItem = customItem;
    this.itemStack = itemStack;
    this.isNormalAttack = isNormalAttack;
    this.enemy = target;

    // 通常攻撃の時はダメージを修正する
    if (isNormalAttack) {
      // 何らかの効果で上乗せされたダメージ + ダメージ
      this.damage = Math.max(damage - +customItem.getMaterialDamage(), 0) + customItem.getAttackItemDamage(StrengthOperator.getLevel(itemStack));
    } else {
      this.damage = damage;
    }

    this.attackItem = CustomWeaponItemStack2.getInstance(itemStack);
  }

  // CustomWeaponItemStack2
  CustomWeaponItemStack2 attackItem;

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
  LivingEntity enemy;

  /**
   * アイテムタイプを取得
   */
  public ItemType geItemType() {
    return customItem.getAttackType();
  }

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public CombatEntityEvent callEvent() {
    Bukkit.getServer().getPluginManager().callEvent(this);
    return this;
  }

  /**
   * LastDamageMethodTypeに変換したTypeを取得
   *
   * @return
   */
  public LastDamageMethodType geLastDamageMethodType() {
    return LastDamageMethodType.fromAttackType(geItemType(), isNormalAttack);
  }

  /**
   * CustomWeaponItemStackを取得
   *
   * @return
   */
  public CustomWeaponItemStack2 getCustomWeaponItemStack() {
    return attackItem;
  }

}
