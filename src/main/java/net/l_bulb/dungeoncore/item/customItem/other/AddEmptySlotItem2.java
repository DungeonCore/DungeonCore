package net.l_bulb.dungeoncore.item.customItem.other;

import net.l_bulb.dungeoncore.item.slot.SlotLevel;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class AddEmptySlotItem2 extends AddEmptySlotItem {
  @Override
  public String getItemName() {
    return "シーグル";
  }

  @Override
  public String getId() {
    return super.getId() + "2";
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.ADD_EMPTY2;
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
    return item;
  }
}
