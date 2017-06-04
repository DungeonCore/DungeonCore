package net.l_bulb.dungeoncore.common.dropingEntity;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
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
public class CombatEntityEvent extends PlayerEvent implements Cancellable {

  /**
   * @param attacker 攻撃を与えたEntity
   * @param damage ダメージ
   * @param customItem 攻撃に使用したカスタムアイテム
   * @param itemStack 攻撃に使用したアイテム
   * @param target 攻撃対象
   * @param isNormalAttack 通常攻撃ならTRUE
   */
  public CombatEntityEvent(Player attacker, double damage, ItemInterface customItem, ItemStack itemStack, boolean isNormalAttack,
      LivingEntity target) {
    super(attacker);
    this.attacker = attacker;
    this.customItem = customItem;
    this.itemStack = itemStack;
    this.isNormalAttack = isNormalAttack;
    this.enemy = target;

    // 通常攻撃の時はダメージを修正する
    if (isNormalAttack && ItemManager.isImplemental(CombatItemable.class, customItem)) {
      // 何らかの効果で上乗せされたダメージ + ダメージ
      this.damage = Math.max(damage - ((CombatItemable) customItem).getMaterialDamage(), 0)
          + new ItemStackNbttagAccessor(itemStack).getDamage();
    } else {
      this.damage = damage;
    }

  }

  // CustomWeaponItemStack2

  // 攻撃者
  Player attacker;

  // 与えたダメージ
  double damage;

  // 使用したスタムアイテム
  ItemInterface customItem;

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

  private boolean isCancel;

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

  @Override
  public boolean isCancelled() {
    return isCancel;
  }

  @Override
  public void setCancelled(boolean isCancel) {
    this.isCancel = isCancel;
  }

  /**
   * イベントを呼ばれた後にダメージの処理を行う
   */
  public void damageEntity() {
    if (isCancelled()) {
      enemy.damage(getDamage());
    }
  }
}
