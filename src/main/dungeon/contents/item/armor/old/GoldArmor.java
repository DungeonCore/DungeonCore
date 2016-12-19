package main.dungeon.contents.item.armor.old;

import main.item.ItemManager;

import org.bukkit.Material;

public class GoldArmor extends LeatherArmor{
	public GoldArmor(Material m, String partsName) {
		super(m, partsName);
	}

	public static void regist() {
		ItemManager.registItem(new GoldArmor(Material.GOLD_HELMET, "HELMET"));
		ItemManager.registItem(new GoldArmor(Material.GOLD_CHESTPLATE, "CHESTPLATE"));
		ItemManager.registItem(new GoldArmor(Material.GOLD_LEGGINGS, "LEGGINGS"));
		ItemManager.registItem(new GoldArmor(Material.GOLD_BOOTS, "BOOTS"));
	}

	@Override
	protected String getMaterialName() {
		return "GOLD";
	}
}
