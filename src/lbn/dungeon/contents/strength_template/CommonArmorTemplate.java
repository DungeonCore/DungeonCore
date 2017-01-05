package lbn.dungeon.contents.strength_template;

import lbn.item.armoritem.ArmorMaterial;

import org.bukkit.inventory.ItemStack;

public class CommonArmorTemplate implements StrengthTemplate{

	ArmorMaterial m;
	int maxStrengthCount;
	public CommonArmorTemplate(int maxStrengthCount, ArmorMaterial m) {
		this.maxStrengthCount = maxStrengthCount;
		this.m = m;
	}

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return null;
	}

	@Override
	public int getStrengthGalions(int level) {
		switch (m) {
		case LEATHER:
			return 60;
		case GOLD:
			return 70;
		case CHAINMAIL:
			return 80;
		case IRON:
			return 90;
		case DIAMOND:
			return 100;
		default:
			break;
		}
		return 100;
	}

	@Override
	public int successChance(int level) {
		if (level <= 1) {
			return 100;
		} else if (level == 2) {
			return 98;
		} else if (level == 3) {
			return 95;
		} else if (level == 4) {
			return 90;
		} else if (level == 5) {
			return 85;
		} else if (level == 6) {
			return 80;
		}
		return 83;
	}

}
