package lbn.item;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.util.ItemStackUtil;

public class GalionItem extends AbstractItem{

	public GalionItem(int galions) {
		this.galions = galions;
	}

	public GalionItem(ItemStack stack) {
		List<String> lore = ItemStackUtil.getLore(stack);
		for (String string : lore) {
			if (string.contains("+") && string.contains("galions")) {
				String replace = string.replace("+", "").replace("galions", "");
				this.galions = Integer.parseInt(ChatColor.stripColor(replace).trim());
			}
		}
	}

	@Override
	public String getItemName() {
		return ChatColor.GOLD + "Money";
	}

	@Override
	public String getId() {
		return "galions";
	}

	@Override
	protected Material getMaterial() {
		return Material.GOLD_INGOT;
	}

	int galions = 0;

	@Override
	protected String[] getDetail() {
		return new String[]{"+ " + galions + " galions"};
	}

	public int getGalions() {
		return galions;
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return getGalions();
	}

}
