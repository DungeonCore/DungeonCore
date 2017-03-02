package lbn.item.implementation.pic;

import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WoodPickAxe extends AbstractPickaxe{

	private static final StonePickaxe STONE_PICKAXE = new StonePickaxe();

	@Override
	public int getBuyPrice(ItemStack item) {
		return 10;
	}

	@Override
	String getMaterialName() {
		return "木";
	}

	@Override
	public AbstractPickaxe getNextPickAxe() {
		return STONE_PICKAXE;
	}

	@Override
	public short getMaxLevel() {
		return 10;
	}

	@Override
	public boolean canDestory(MagicStoneOreType type) {
		return type == MagicStoneOreType.COAL_ORE;
	}

//	@Override
//	protected ItemStack getItemStackBase() {
//		return ItemStackUtil.getItemStackByCommand("give @p minecraft:wooden_pickaxe 1 0 {Unbreakable:1,CanDestroy:[\"minecraft:coal_ore\"]}");
//	}

	@Override
	protected Material getMaterial() {
		return Material.WOOD_PICKAXE;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"鉱石を採掘するとレベルが上がります", "石炭鉱石を採掘できる"};
	}

	@Override
	public int getLapisCount(short level) {
		return 1;
	}

	@Override
	public String getGiveItemId() {
		return "wooden_pickaxe";
	}
}
