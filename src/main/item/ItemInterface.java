package main.item;

import main.player.AttackType;

import org.bukkit.inventory.ItemStack;

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
