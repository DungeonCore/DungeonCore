package lbn.item.craft;

import lbn.item.ItemLoreToken;
import lbn.item.itemInterface.CraftItemable;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.StrengthOperator;

import org.bukkit.inventory.ItemStack;


public class CraftViewerItems {
	public static ItemStack getViewItem(CraftItemable craftItem) {
		TheLowCraftRecipeInterface craftRecipe = craftItem.getCraftRecipe();

		if (craftItem instanceof Strengthenable) {
			ItemLoreToken strengthLoreToken = StrengthOperator.getStrengthLoreToken((Strengthenable) craftItem, 0);
		}
		return null;
	}
}
