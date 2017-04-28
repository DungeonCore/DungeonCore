package net.l_bulb.dungeoncore.item;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.player.ItemType;

public interface ItemInterface {
  String getItemName();

  boolean isShowItemList();

  String getSimpleName();

  ItemStack getItem();

  boolean isThisItem(ItemStack item);

  ItemType getAttackType();

  boolean isQuestItem();

  String getId();

  int getBuyPrice(ItemStack item);

  boolean isStrengthItem();
}
