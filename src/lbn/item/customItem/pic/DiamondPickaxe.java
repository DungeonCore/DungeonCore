package lbn.item.customItem.pic;

import java.util.ArrayList;
import java.util.List;

import lbn.item.ItemInterface;
import lbn.item.lore.ItemLoreToken;
import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondPickaxe extends AbstractPickaxe{

	public DiamondPickaxe(int level) {
		super(level);
	}

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
		int nextLevel = level + 1;

		if (nextLevel >= 11) {
			return null;
		} else {
			return new IronPickaxe(nextLevel);
		}
	}

	@Override
	public short getMaxExp() {
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
	public ItemLoreToken getStandardLoreToken() {
		ItemLoreToken loreToken = super.getStandardLoreToken();
		loreToken.addLore("最大レベル時、ラピスを7個まで取得できる");
		return loreToken;
	}

	@Override
	public int getLapisCount(short level) {
		if (level == getMaxExp()) {
			return 7;
		}
		return 4;
	}

	@Override
	public String getGiveItemId() {
		return "diamond_pickaxe";
	}

	public List<ItemInterface> getAllLevelPick(){
		ArrayList<ItemInterface> woodPicks = new ArrayList<ItemInterface>();
		for (int i = 1; i <= 10; i++) {
			woodPicks.add(new DiamondPickaxe(i));
		}
		return woodPicks;
	}

}
