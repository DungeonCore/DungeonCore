package main.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public class BowLevel12 implements StrengthTemplate {

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return new ItemStack[]{};
	}

	@Override
	public int successChance(int level) {
		switch (level) {
		case 0:
			break;
		case 1:
			return 100;
		case 2:
			return 95;
		case 3:
			return 85;
		case 4:
			return 72;
		case 5:
			return 60;
		case 6:
			return 45;
		case 7:
			return 30;
		case 8:
			return 24;
		case 9:
			return 18;
		case 10:
			return 10;
		case 11:
			return 7;
		case 12:
			return 3;
		default:
			break;
		}
		return 100;
	}

	@Override
	public int getStrengthGalions(int level) {
		return 50 * level;
	}

}
