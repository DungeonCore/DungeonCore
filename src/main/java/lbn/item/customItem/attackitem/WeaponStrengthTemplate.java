package lbn.item.customItem.attackitem;

import org.bukkit.inventory.ItemStack;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.customItem.other.StrengthScrollWeapon;

public class WeaponStrengthTemplate implements StrengthTemplate {
  static ItemStack strengthScrollWeapon = new StrengthScrollWeapon().getItem();

  @Override
  public ItemStack getStrengthMaterials(int level) {
    return strengthScrollWeapon;
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
