package lbn.dungeon.contents.item.armor.old;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemManager;

public class DiamondArmor extends LeatherArmor{

	public DiamondArmor(Material m, String partsName) {
		super(m, partsName);
	}

	public static void regist() {
		ItemManager.registItem(new DiamondArmor(Material.DIAMOND_HELMET, "HELMET"));
		ItemManager.registItem(new DiamondArmor(Material.DIAMOND_BOOTS, "BOOTS"));
	}

	@Override
	protected String getMaterialName() {
		return "DIAMOMD";
	}

	@Override
	protected void enchant(ItemStack item) {
	}
}
