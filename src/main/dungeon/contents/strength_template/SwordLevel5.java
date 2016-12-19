package main.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public class SwordLevel5 extends AbstractSelectMaxMinStrengthTemplate{
	SwordLevel5() {
		super(5);
	}

	public static SwordLevel5 getInstance() {
		return new SwordLevel5();
	}

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return null;
	}

	@Override
	protected double getSuccessRateLevel0() {
		return 100;
	}

	@Override
	protected double getSuccessRateLevelMax() {
		return 60;
	}

	@Override
	public int getStrengthGalions(int level) {
		return 100;
	}

}
