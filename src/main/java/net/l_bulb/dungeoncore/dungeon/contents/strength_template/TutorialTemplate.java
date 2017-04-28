package net.l_bulb.dungeoncore.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public class TutorialTemplate implements StrengthTemplate {

  @Override
  public ItemStack getStrengthMaterials(int level) {
    return null;
  }

  @Override
  public int getStrengthGalions(int level) {
    return 0;
  }

  @Override
  public int successChance(int level) {
    return 100;
  }

}
