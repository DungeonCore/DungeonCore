package net.l_bulb.dungeoncore.item.customItem.other;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.SlotType;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;

public class AddEmptySlotItem extends AbstractItem implements SlotInterface {

  @Override
  public String getItemName() {
    return "精錬されたシーグル";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return getLevel().getPrice();
  }

  @Override
  public String getSlotName() {
    return "add_empty_slot";
  }

  @Override
  public String getSlotDetail() {
    return "空のスロットを追加する";
  }

  @Override
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = super.getStandardLoreToken();
    loreToken.addLore("装着成功確率 : " + getLevel().getSucessPer() + "% ");
    return loreToken;
  }

  @Override
  public String getId() {
    return "add_empty_slot";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.RED;
  }

  @Override
  public SlotType getSlotType() {
    return SlotType.ADD_EMPTY;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.ADD_EMPTY;
  }

  @Override
  public boolean isSame(SlotInterface slot) {
    return slot.getClass().equals(AddEmptySlotItem.class);
  }

  @Override
  protected Material getMaterial() {
    return Material.FLINT_AND_STEEL;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "鍛冶屋の魔法石装着画面で", "武器に空のスロットを追加できます" };
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
    return item;
  }

}
