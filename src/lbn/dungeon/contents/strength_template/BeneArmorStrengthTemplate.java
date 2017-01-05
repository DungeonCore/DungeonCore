package lbn.dungeon.contents.strength_template;

import lbn.dungeon.contents.item.material.ArmorMaterialItem;
import lbn.item.armoritem.ArmorMaterial;

import org.bukkit.inventory.ItemStack;

public class BeneArmorStrengthTemplate implements StrengthTemplate{
	public BeneArmorStrengthTemplate(ArmorMaterial am) {
		this.am = am;
	}

	ArmorMaterial am;

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return new ItemStack[]{new ArmorMaterialItem(am).getItem()};
	}

	@Override
	public int getStrengthGalions(int level) {
		switch (am) {
		case LEATHER:
			return 60;
		case GOLD:
			return 75;
		case CHAINMAIL:
			return 100;
		case IRON:
			return 130;
		case DIAMOND:
			return 180;
		default:
			break;
		}
		return 120;
	}

	@Override
	public int successChance(int level) {
		if (level < 2) {
			return 88;
		} else {
			return 73;
		}
	}

}
