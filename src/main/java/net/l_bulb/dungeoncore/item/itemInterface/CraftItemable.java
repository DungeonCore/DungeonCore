package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeInterface;

public interface CraftItemable extends ItemInterface {
  public TheLowCraftRecipeInterface getCraftRecipe();
}
