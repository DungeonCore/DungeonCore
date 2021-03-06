package net.l_bulb.dungeoncore.item.customItem.armoritem;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.customItem.other.StrengthScrollArmor;

public class ArmorStrengthTemplate implements StrengthTemplate {
  static ItemStack strengthScrollArmor = new StrengthScrollArmor().getItem();

  @Override
  public ItemStack getStrengthMaterials(int level) {
    return strengthScrollArmor;
  }

  @Override
  public int getStrengthGalions(int level) {
    switch (level) {
      case 0:
        return 50;
      case 1:
        return 50;
      case 2:
        return 100;
      case 3:
        return 300;
      case 4:
        return 500;
      case 5:
        return 800;
      case 6:
        return 1300;
      case 7:
        return 1900;
      case 8:
        return 2000;
      case 9:
        return 2100;
      case 10:
        return 2200;
      default:
        break;
    }
    return 2200;
  }

  @Override
  public int successChance(int level) {
    switch (level) {
      case 0:
      case 1:
        return 98;
      case 2:
        return 86;
      case 3:
        return 70;
      case 4:
        return 53;
      case 5:
        return 38;
      case 6:
        return 20;
      case 7:
        return 80;
      case 8:
        return 75;
      case 9:
        return 65;
      case 10:
        return 50;
      default:
        break;
    }
    return 50;
  }

}
