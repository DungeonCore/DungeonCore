package lbn.dungeon.contents.item.armor.old;

import org.bukkit.Material;

import lbn.item.ItemManager;

public class IronArmor extends ChainArmor{

	public IronArmor(Material m, String partsName) {
		super(m, partsName);
	}

	public static void regist() {
		ItemManager.registItem(new IronArmor(Material.IRON_HELMET, "HELMET"));
		ItemManager.registItem(new IronArmor(Material.IRON_CHESTPLATE, "CHESTPLATE"));
		ItemManager.registItem(new IronArmor(Material.IRON_LEGGINGS, "LEGGINGS"));
		ItemManager.registItem(new IronArmor(Material.IRON_BOOTS, "BOOTS"));
	}

	@Override
	protected String getMaterialName() {
		return "IRON";
	}

}
