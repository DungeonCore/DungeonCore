package lbn.item.itemInterface;

import lbn.item.ItemInterface;
import lbn.item.system.craft.TheLowCraftRecipeInterface;

public interface CraftItemable extends ItemInterface{
	public TheLowCraftRecipeInterface getCraftRecipe();
}
