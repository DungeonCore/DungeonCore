package lbn.item;

import lbn.player.ItemType;

import org.bukkit.inventory.ItemStack;

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
