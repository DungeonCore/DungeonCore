package net.l_bulb.dungeoncore.item.slot;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;

public abstract class AbstractSlot extends AbstractItem implements SlotInterface {
  @Override
  public SlotType getSlotType() {
    return SlotType.NORMAL;
  }

  @Override
  public String getItemName() {
    return StringUtils.join(new Object[] { ChatColor.WHITE, "魔法石[", getNameColor(), getSlotName(), ChatColor.WHITE, "]" });
  }

  @Override
  public String[] getDetail() {
    if (getSlotDetail() == null) { return new String[0]; }
    return getSlotDetail().split(",");
  }

  @Override
  public ItemLoreToken getStandardLoreToken(ItemStackNbttagAccessor newParam) {
    ItemLoreToken loreToken = super.getStandardLoreToken(newParam);
    loreToken.addLore("レア度 : " + getLevel().getStar());
    loreToken.addLore("装着成功確率 : " + getLevel().getSucessPer() + "% ");
    return loreToken;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return getLevel().getPrice();
  }

  @Override
  protected Material getMaterial() {
    return Material.INK_SACK;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    item.setDurability(getLevel().getData());
    MaterialData data = item.getData();
    // データ値をセット
    data.setData(getLevel().getData());
    item.setData(data);

    // エンチャントをセット
    item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

    return item;
  }

  @Override
  public boolean isSame(SlotInterface slot) {
    if (slot != null) { return getId().equals(slot.getId()); }
    return false;
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof SlotInterface) {
      return getId().equals(((SlotInterface) obj).getId());
    } else {
      return false;
    }
  }
}
