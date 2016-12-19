package lbn.item;

import org.bukkit.inventory.ItemStack;

import lbn.player.AttackType;

public interface ItemInterface{
	String getItemName();

	boolean isDispList();

	String getSimpleName();

	ItemStack getItem();

	boolean isThisItem(ItemStack item);

	AttackType getAttackType();

	boolean isQuestItem();

	String getId();

	int getBuyPrice(ItemStack item);
}
