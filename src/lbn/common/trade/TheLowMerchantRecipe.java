package lbn.common.trade;

import net.minecraft.server.v1_8_R1.MerchantRecipe;

import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class TheLowMerchantRecipe {
	public TheLowMerchantRecipe(MerchantRecipe recipe) {
		buy1 = CraftItemStack.asBukkitCopy(recipe.getBuyItem1());
		buy2 = CraftItemStack.asBukkitCopy(recipe.getBuyItem2());
		result = CraftItemStack.asBukkitCopy(recipe.getBuyItem3());
	}

	public TheLowMerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack result) {
		this.buy1 = buy1;
		this.buy2 = buy2;
		this.result = result;
	}

	ItemStack buy1;
	ItemStack buy2;
	ItemStack result;

	public TheLowMerchantRecipe(ItemStack buy1, ItemStack result) {
		this(buy1, null, result);
	}

	public ItemStack getBuy1() {
		return buy1;
	}

	public ItemStack getBuy2() {
		return buy2;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setBuy1(ItemStack buy1) {
		this.buy1 = buy1;
	}

	public void setBuy2(ItemStack buy2) {
		this.buy2 = buy2;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

	public MerchantRecipe toMerchantRecipe() {
		return new MerchantRecipe(CraftItemStack.asNMSCopy(buy1), CraftItemStack.asNMSCopy(buy2), CraftItemStack.asNMSCopy(result));
	}

}
