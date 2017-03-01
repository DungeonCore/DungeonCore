package lbn.item.implementation;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.AbstractItem;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.player.magicstoneOre.MagicStoneOreType;

public class MagicStoneOre extends AbstractItem{

	MagicStoneOreType type;

	/**
	 * MagicStoneOreTypeからインスタンスを取得する
	 * @param type
	 * @return
	 */
	public static ItemInterface getMagicStoneOre(MagicStoneOreType type) {
		//登録されている時はそれを取得
		ItemInterface customItemById = ItemManager.getCustomItemById("ore_" + type.toString().toLowerCase());
		if (customItemById != null) {
			return customItemById;
		}
		return new MagicStoneOre(type);
	}

	/**
	 * 魔法鉱石の種類
	 * @param type
	 */
	public MagicStoneOre(MagicStoneOreType type) {
		this.type = type;
	}

	@Override
	public String getItemName() {
		if (type == MagicStoneOreType.COAL_ORE) {
			return "石炭";
		}
		return type.getJpName();
	}

	@Override
	public String getId() {
		return "ore_" + type.toString().toLowerCase();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		if (type == MagicStoneOreType.COAL_ORE) {
			return 50;
		}
		return 300;
	}

	@Override
	protected Material getMaterial() {
		if (type == MagicStoneOreType.COAL_ORE) {
			return Material.COAL;
		}
		return type.getMaterial();
	}

	@Override
	public String[] getDetail() {
		if (type == MagicStoneOreType.COAL_ORE) {
			return new String[]{"魔法鉱石を精錬するときに使います"};
		}
		return new String[]{"精錬すると魔法石になります"};
	}

}
