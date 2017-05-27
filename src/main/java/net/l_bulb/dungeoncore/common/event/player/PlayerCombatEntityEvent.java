package net.l_bulb.dungeoncore.common.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.player.ItemType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCombatEntityEvent extends TheLowPlayerEvent {

  /**
   * @param player 攻撃を与えたPlayer
   * @param damage ダメージ
   * @param customItem 攻撃に使用したカスタムアイテム
   * @param itemStack 攻撃に使用したアイテム
   * @param target 攻撃対象
   * @param isNormalAttack 通常攻撃ならTRUE
   */
  public PlayerCombatEntityEvent(TheLowPlayer player, double damage, ItemInterface customItem, ItemStack itemStack, boolean isNormalAttack,
      Entity target) {
    super(player);
    this.damage = damage;
    this.customItem = customItem;
    this.itemStack = itemStack;
    this.isNormalAttack = isNormalAttack;
    this.target = target;
  }

  /**
   * @param player 攻撃を与えたPlayer
   * @param damage ダメージ
   * @param item 攻撃に使用したアイテム
   * @param target 攻撃対象
   * @param isNormalAttack 通常攻撃ならTRUE
   */
  public PlayerCombatEntityEvent(TheLowPlayer player, double damage, ItemStack item, boolean isNormalAttack, Entity target) {
    this(player, damage, ItemManager.getCustomItem(item), item, isNormalAttack, target);
  }

  // 与えたダメージ
  double damage;

  // 使用したスタムアイテム
  ItemInterface customItem;

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

}
