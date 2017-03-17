package lbn.item;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import lbn.item.craft.TheLowCraftRecipeWithMainItem;
import lbn.item.craft.TheLowCraftRecipeInterface;
import lbn.util.ItemStackUtil;

public class CraftMaterialHolder {
	static HashMap<String, TheLowCraftRecipeWithMainItem> recipeMap = new HashMap<String, TheLowCraftRecipeWithMainItem>();

	/**
	 * ItemInterfaceからレシピを取得
	 * @param item
	 * @return
	 */
	public TheLowCraftRecipeInterface getCraftRecipe(ItemInterface item) {
		String id = item.getId();
		TheLowCraftRecipeInterface theLowCraftRecipe = recipeMap.get(id);
		return theLowCraftRecipe;
	}

	/**
	 * ItemStackからレシピを取得
	 * @param item
	 * @return
	 */
	public TheLowCraftRecipeInterface getCraftRecipe(ItemStack item) {
		String id = ItemStackUtil.getId(item);
		TheLowCraftRecipeInterface theLowCraftRecipe = recipeMap.get(id);
		return theLowCraftRecipe;
	}
}
