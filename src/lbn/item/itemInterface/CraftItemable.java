package lbn.item.itemInterface;

import lbn.item.ItemInterface;
import lbn.item.craft.TheLowCraftRecipeInterface;

public interface CraftItemable extends ItemInterface{
	public TheLowCraftRecipeInterface getCraftRecipe();
}
