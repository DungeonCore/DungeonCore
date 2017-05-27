package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.CustomWeaponItemStack2;

public interface CombatItemable extends RightClickItemable, EquipItemable {
  /**
   * Entityを攻撃した時
   *
   * @param e
   */
  void onCombatEntity(CombatEntityEvent e);

  /**
   * アイテムを捨てる時
   *
   * @param e
   */
  void onPlayerDropItemEvent(PlayerDropItemEvent e);

  /**
   * デフォルトのスロットの数
   *
   * @return
   */
  int getDefaultSlotCount();

  /**
   * デフォルトのスロットの数
   *
   * @return
   */
  int getMaxSlotCount();

  /**
   * 武器のダメージを取得 (武器本体のダメージも含まれます)
   *
   * @param p
   * @param strengthLevel 強化レベル
   * @return
   */
  double getAttackItemDamage(int strengthLevel);

  /**
   * この武器のアイテムのデフォルトの攻撃力を取得
   *
   * @return
   */
  double getMaterialDamage();

  /**
   * カスタムアイテムスタックを取得する
   *
   * @return
   */
  default CustomWeaponItemStack2 getCombatAttackItemStack(ItemStack item) {
    return CustomWeaponItemStack2.getInstance(item, this);
  }
}
