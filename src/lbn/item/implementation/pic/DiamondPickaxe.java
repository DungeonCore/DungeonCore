package lbn.item.implementation.pic;

import java.util.List;

import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondPickaxe extends AbstractPickaxe{

	@Override
	public int getBuyPrice(ItemStack item) {
		return 1000;
	}

	@Override
	String getMaterialName() {
		return "ダイヤ";
	}

	@Override
	public AbstractPickaxe getNextPickAxe() {
		return null;
	}

	@Override
	public short getMaxLevel() {
		return 100;
	}

	@Override
	public boolean canDestory(MagicStoneOreType type) {
		return true;
	}

	@Override
	protected Material getMaterial() {
		return Material.DIAMOND_PICKAXE;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"鉱石を採掘するとレベルが上がります", "全ての鉱石を採掘できます"};
	}

	@Override
	protected List<String> getAddDetail() {
		List<String> addDetail = super.getAddDetail();
		addDetail.add("最大レベル時、ラピスを7個まで取得できる");
		return addDetail;
	}

	@Override
	public int getLapisCount(short level) {
		if (level == getMaxLevel()) {
			return 7;
		}
		return 4;
	}

	@Override
	public String getGiveItemId() {
		return "diamond_pickaxe";
	}

}
