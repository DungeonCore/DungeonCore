package lbn.dungeon.contents.item.armor.old;

import lbn.item.ItemManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ChainArmor extends LeatherArmor{

	public ChainArmor(Material m, String partsName) {
		super(m, partsName);
	}

	public static void regist() {
		ItemManager.registItem(new ChainArmor(Material.CHAINMAIL_HELMET, "HELMET"));
		ItemManager.registItem(new ChainArmor(Material.CHAINMAIL_CHESTPLATE, "CHESTPLATE"));
		ItemManager.registItem(new ChainArmor(Material.CHAINMAIL_LEGGINGS, "LEGGINGS"));
		ItemManager.registItem(new ChainArmor(Material.CHAINMAIL_BOOTS, "BOOTS"));
	}

	@Override
	protected String getMaterialName() {
		return "Chain";
	}

	@Override
	protected void enchant(ItemStack item) {
		item.addEnchantment(Enchantment.DURABILITY, 1);
	}
}
