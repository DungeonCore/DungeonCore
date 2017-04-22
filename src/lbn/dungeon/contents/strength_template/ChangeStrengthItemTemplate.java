package lbn.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public class ChangeStrengthItemTemplate implements StrengthTemplate {

	ItemStack material;
	int needGalion;
	int successRate;

	public ChangeStrengthItemTemplate(ItemStack material, int needGalion, int suucessRate) {
		this.material = material;
		this.needGalion = needGalion;
		this.successRate = suucessRate;
	}

	@Override
	public ItemStack getStrengthMaterials(int level) {
		return material;
	}

	@Override
	public int getStrengthGalions(int level) {
		return needGalion;
	}

	@Override
	public int successChance(int level) {
		return successRate;
	}

}
