package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.other.ItemStackData;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.player.ItemType;

public interface WeaponSkillInterface {
  /**
   * スキルレベルを取得
   *
   * @return
   */
  int getSkillLevel();

  /**
   * スキル名を取得
   *
   * @return
   */
  String getName();

  /**
   * スキルIDを取得
   *
   * @return
   */
  String getId();

  /**
   * スキルの説明を取得
   *
   * @return
   */
  String[] getDetail();

  /**
   * スキル選択後にクリックした時の処理
   *
   * @param p 実行者のPlayer
   * @param item クリックしたアイテム
   * @param customItem クリックしたアイテムのカスタムアイテム
   * @return スキル発動完了処理を行うならTRUE
   */
  boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem);

  /**
   * スキル選択後に戦闘をした時の処理
   *
   * @param p
   * @param item
   * @param customItem
   * @param livingEntity
   */
  void onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, CombatEntityEvent event);

  /**
   * スキルのクールタイムを取得
   *
   * @return
   */
  int getCooltime();

  /**
   * 消費MPを取得
   *
   * @return
   */
  int getNeedMagicPoint();

  /**
   * 一覧を表示するためのItemStackDataを取得
   *
   * @return
   */
  ItemStackData getViewItemStackData();

  /**
   * もし指定したタイプの武器で使えるならTRUE
   *
   * @param type
   * @return
   */
  boolean canUse(ItemType type);
}
