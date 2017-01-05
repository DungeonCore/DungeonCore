package lbn.item.setItem;

import java.util.ArrayList;
import java.util.List;

import lbn.item.ItemInterface;
import lbn.item.strength.StrengthOperator;
import lbn.player.AttackType;
import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetItemParts implements ItemInterface{
	SetItemInterface belongSetItem;
	Material material;
	SetItemPartsType type;

	@Override
	public boolean isDispList() {
		return true;
	}

	public SetItemParts(SetItemInterface belongSetItem, Material material, SetItemPartsType type) {
		super();
		this.belongSetItem = belongSetItem;
		this.material = material;
		this.type = type;
	}

	public SetItemInterface getBelongSetItem() {
		return belongSetItem;
	}

	public Material getMaterial() {
		return material;
	}

	public SetItemPartsType getItemSetPartsType() {
		return type;
	}

	@Override
	public boolean isThisItem(ItemStack item) {
		if (item == null) {
			return false;
		}

		if (item.getType() != getItem().getType()) {
			return false;
		}
		List<String> lore = ItemStackUtil.getLore(item);
		if (lore.size() == 0) {
			return false;
		}
		if (!lore.get(0).contains(getBelongSetItem().getName())) {
			return false;
		}

		return true;
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(getMaterial());
		ItemStackUtil.setDispName(itemStack, getItemName());

		ArrayList<String> lore = new ArrayList<String>();
		lore.add("SET:" + getBelongSetItem().getName());
		lore.add(ChatColor.GRAY + ItemStackUtil.getLoreForIdLine(getId()));
		lore.add("");
		lore.addAll(getBelongSetItem().getLore());

		ItemStackUtil.addLore(itemStack, lore.toArray(new String[0]));
		return itemStack;
	}

	@Override
	public String getItemName() {
		return getBelongSetItem().getName() + "  -" + getItemSetPartsType() + "-";
	}

	@Override
	public boolean equals(Object paramObject) {
		if (paramObject instanceof ItemInterface) {
			return getItemName().equals(((ItemInterface) paramObject).getItemName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getItemName().hashCode();
	}

	public static String getSetItemNameByItem(ItemStack item) {
		List<String> lore = ItemStackUtil.getLore(item);
		//説明が１行以上
		if (lore.size() > 0) {
			String nameTag = lore.get(0);
			//"SET:"が含まれていたらsetitemとして処理する
			if (nameTag.contains("SET:")) {
				return nameTag.replace("SET:", "");
			}
		}
		return null;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.IGNORE;
	}

	@Override
	public String getSimpleName() {
		return ChatColor.stripColor(getItemName());
	}

	@Override
	public boolean isQuestItem() {
		return false;
	}

	@Override
	public String getId() {
		return (belongSetItem.getName() + " " + getItemSetPartsType()).toLowerCase();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 200 * (StrengthOperator.getLevel(item) + 1);
	}
}
