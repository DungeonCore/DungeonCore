package net.l_bulb.dungeoncore.item.customItem.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;

public class StrengthScrollArmor extends AbstractItem {

  @Override
  public String getItemName() {
    return "強化スクロール (防具)";
  }

  @Override
  public String getId() {
    return "strength_scroll_a";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 500;
  }

  @Override
  protected Material getMaterial() {
    return Material.PAPER;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "防具の強化の際に使うアイテム" };
  }

}
