package lbn.item.slot;

import lbn.item.AbstractItem;
import lbn.item.ItemLoreToken;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public abstract class AbstractSlot extends AbstractItem implements SlotInterface{
	public SlotType getSlotType() {
		return SlotType.NORMAL;
	}

	@Override
	public String getItemName() {
		return StringUtils.join(new Object[]{ ChatColor.WHITE , "魔法石[" , getNameColor() , getSlotName() , ChatColor.WHITE , "]"});
	}

	@Override
	public String[] getDetail() {
		if (getSlotDetail() == null) {
			return new String[0];
		}
		return getSlotDetail().split(",");
	}

	@Override
	public ItemLoreToken getStandardLoreToken() {
		ItemLoreToken loreToken = super.getStandardLoreToken();
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
		//データ値をセット
		data.setData(getLevel().getData());
		item.setData(data);

		//エンチャントをセット
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

		return item;
	}

	@Override
	public boolean isSame(SlotInterface slot) {
		if (slot != null) {
			return getId().equals(slot.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SlotInterface) {
			return getId().equals(((SlotInterface)obj).getId());
		} else {
			return false;
		}
	}
}
