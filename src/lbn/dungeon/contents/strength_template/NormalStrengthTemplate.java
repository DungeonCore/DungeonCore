package lbn.dungeon.contents.strength_template;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NormalStrengthTemplate extends AbstractSelectMaxMinStrengthTemplate{

	public NormalStrengthTemplate(int maxStrengthLevel, double successRateLevel0, double successRateLevelMax) {
		super(maxStrengthLevel);
		this.successRateLevel0 = successRateLevel0;
		this.successRateLevelMax = successRateLevelMax;
	}

	double successRateLevel0;
	double successRateLevelMax;

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return new ItemStack[]{new ItemStack(Material.EMERALD, 10)};
	}

	@Override
	protected double getSuccessRateLevel0() {
		return successRateLevel0;
	}

	@Override
	protected double getSuccessRateLevelMax() {
		return successRateLevelMax;
	}

	@Override
	public int getStrengthGalions(int level) {
		return 50 * level + 50;
	}
}
