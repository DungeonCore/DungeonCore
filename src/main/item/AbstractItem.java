package main.item;

import java.util.ArrayList;
import java.util.List;

import main.item.itemInterface.Strengthenable;
import main.item.strength.StrengthOperator;
import main.player.AttackType;
import main.util.ItemStackUtil;
import main.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractItem implements ItemInterface{
	@Override
	public boolean isThisItem(ItemStack item) {
		if (ItemStackUtil.isEmpty(item)) {
			return false;
		}
		return item.getType() == getMaterial()
				&& ChatColor.stripColor(getId()).equals(ItemStackUtil.getId(item));
	}

	@Override
	public boolean isDispList() {
		return true;
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = getItemStackBase();

		ItemStackUtil.addLore(itemStack, ChatColor.DARK_GRAY + ItemStackUtil.getLoreForIdLine(getId()));
		//アイテム名
		ItemStackUtil.setDispName(itemStack, ChatColor.RESET + getItemName());

		//アイテムの説明
		ItemMeta data = itemStack.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		//id付与
		lore.add(ChatColor.GRAY + ItemStackUtil.getLoreForIdLine(getId()));
		if (getDetail() != null) {
			for (String string :  getDetail()) {
				lore.add(ChatColor.AQUA + string);
			}
		}

		lore.add("");
		if (getAddDetail() != null && getAddDetail().size() != 0) {
			lore.add(ChatColor.GREEN + "[基本性能]");
			for (String string : getAddDetail()) {
				lore.add("    " + ChatColor.YELLOW + string);
			}
			lore.add("");
		}

		data.setLore(lore);
		itemStack.setItemMeta(data);

		StrengthOperator.updateLore(itemStack, 0);
		return itemStack;
	}

	protected ItemStack getItemStackBase() {
		ItemStack itemStack = new ItemStack(getMaterial());
		return itemStack;
	}

	abstract protected Material getMaterial();

	abstract protected String[] getDetail();

	protected List<String> getAddDetail() {
		ArrayList<String> lore = new ArrayList<String>();
		if (this instanceof Strengthenable) {
			//最大強化
			if (((Strengthenable)this).getMaxStrengthCount() != 0) {
				lore.add(Message.getMessage("最大強化：+{0}", ((Strengthenable)this).getMaxStrengthCount()));
			}
		}
		return lore;
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
	public String toString() {
		return getItemName();
	}
}
