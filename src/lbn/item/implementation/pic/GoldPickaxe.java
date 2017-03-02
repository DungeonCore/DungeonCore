package lbn.item.implementation.pic;

import java.util.List;

import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldPickaxe extends AbstractPickaxe{

	private static final IronPickaxe IRON_PICKAXE = new IronPickaxe();

	@Override
	public int getBuyPrice(ItemStack item) {
		return 50;
	}

	@Override
	String getMaterialName() {
		return "金";
	}

	@Override
	public AbstractPickaxe getNextPickAxe() {
		return IRON_PICKAXE;
	}

	@Override
	public short getMaxLevel() {
		return 40;
	}

	@Override
	public boolean canDestory(MagicStoneOreType type) {
		switch (type) {
		case COAL_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	protected Material getMaterial() {
		return Material.GOLD_PICKAXE;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"鉱石を採掘するとレベルが上がります", "石炭鉱石・鉄鉱石・ラピス鉱石を", "採掘できます"};
	}

	@Override
	protected List<String> getAddDetail() {
		List<String> addDetail = super.getAddDetail();
		return addDetail;
	}

	@Override
	public int getLapisCount(short level) {
		return 2;
	}

	@Override
	public String getGiveItemId() {
		return "golden_pickaxe";
	}

//	@Override
//	protected ItemStack getItemStackBase() {
//		return ItemStackUtil.getItemStackByCommand("give @p minecraft:gold_pickaxe 1 0 {Unbreakable:1,CanDestroy:[\"minecraft:coal_ore\",\"minecraft:iron_ore\",\"minecraft:lapis_ore\"]}");
//	}

}
