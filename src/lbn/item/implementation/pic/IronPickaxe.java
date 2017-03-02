package lbn.item.implementation.pic;

import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronPickaxe extends AbstractPickaxe{

	private static final DiamondPickaxe DIAMOND_PICKAXE = new DiamondPickaxe();

	@Override
	public int getBuyPrice(ItemStack item) {
		return 200;
	}

	@Override
	String getMaterialName() {
		return "鉄";
	}

	@Override
	public AbstractPickaxe getNextPickAxe() {
		return DIAMOND_PICKAXE;
	}

	@Override
	public short getMaxLevel() {
		return 50;
	}

	@Override
	public boolean canDestory(MagicStoneOreType type) {
		switch (type) {
		case COAL_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
		case REDSTONE_ORE:
		case GOLD_ORE:
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	protected Material getMaterial() {
		return Material.IRON_PICKAXE;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"鉱石を採掘するとレベルが上がります", "石炭鉱石・鉄鉱石・ラピス鉱石・", "レッドストーン鉱石・金鉱石を採掘できます"};
	}

	@Override
	public int getLapisCount(short level) {
		return 3;
	}

	@Override
	public String getGiveItemId() {
		return "iron_pickaxe";
	}
}
