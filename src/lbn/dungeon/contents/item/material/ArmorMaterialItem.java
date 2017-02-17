package lbn.dungeon.contents.item.material;

import java.util.ArrayList;
import java.util.List;

import lbn.item.AbstractItem;
import lbn.item.ItemInterface;
import lbn.item.armoritem.ArmorMaterial;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ArmorMaterialItem extends AbstractItem{
	ArmorMaterial m;
	public ArmorMaterialItem(ArmorMaterial m) {
		this.m = m;
	}

	public static List<ItemInterface> getAllItems() {
		ArrayList<ItemInterface> arrayList = new ArrayList<ItemInterface>();
		for (ArmorMaterial am : ArmorMaterial.values()) {
			arrayList.add(new ArmorMaterialItem(am));
		}
		return arrayList;
	}


	@Override
	public String getItemName() {
		if (getMaterial() == Material.IRON_FENCE) {
			return "BENE " + "CHAINMAIL INGOT";
		} else {
			return "BENE " + getMaterial().toString().replace("_", " ");
		}
	}

	@Override
	public String getId() {
		return getItemName().replace(" ", "_").toLowerCase();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		switch (m) {
		case LEATHER:
			return 100;
		case GOLD:
			return 220;
		case CHAINMAIL:
			return 380;
		case IRON:
			return 600;
		case DIAMOND:
			return 900;
		default:
			return 100;
		}
	}

	@Override
	protected Material getMaterial() {
		switch (m) {
		case LEATHER:
			return Material.LEATHER;
		case GOLD:
			return Material.GOLD_INGOT;
		case CHAINMAIL:
			return Material.IRON_FENCE;
		case IRON:
			return Material.IRON_INGOT;
		case DIAMOND:
			return Material.DIAMOND;
		default:
			break;
		}
		return null;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return item;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"Bene装備の強化に使用します。", "防具は最大強化時にBene装備へと進化します。"};
	}

}
