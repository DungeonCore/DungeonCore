package net.l_bulb.dungeoncore.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public interface StrengthTemplate {
  public ItemStack getStrengthMaterials(int level);

  public int getStrengthGalions(int level);

  public int successChance(int level);
}
