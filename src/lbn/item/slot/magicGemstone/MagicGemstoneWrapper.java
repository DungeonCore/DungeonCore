package lbn.item.slot.magicGemstone;

import lbn.item.ItemInterface;
import lbn.item.slot.SlotLevel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MagicGemstoneWrapper extends MagicGemstone{
	public static MagicGemstoneWrapper LEVEL1_GEMSTONE = new MagicGemstoneWrapper(SlotLevel.LEVEL1, Material.STAINED_CLAY, 7);
	public static MagicGemstoneWrapper LEVEL2_GEMSTONE = new MagicGemstoneWrapper(SlotLevel.LEVEL1, Material.STAINED_CLAY, 12);
	public static MagicGemstoneWrapper LEVEL3_GEMSTONE = new MagicGemstoneWrapper(SlotLevel.LEVEL1, Material.STAINED_CLAY, 9);
	public static MagicGemstoneWrapper LEVEL4_GEMSTONE = new MagicGemstoneWrapper(SlotLevel.LEVEL1, Material.STAINED_CLAY, 3);
	public static MagicGemstoneWrapper LEVEL5_GEMSTONE = new MagicGemstoneWrapper(SlotLevel.LEVEL1, Material.STAINED_CLAY, 11);

	public MagicGemstoneWrapper(SlotLevel level, Material m, int data) {
		this.level = level;
		this.m = m;
		this.data = (byte) data;
	}

	SlotLevel level;
	Material m;
	byte data;

	@Override
	public SlotLevel getSlotLevel() {
		return level;
	}

	@Override
	protected Material getMaterial() {
		return m;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.getData().setData(data);
		return item;
	}

	public static ItemInterface[] getAllItem() {
		return new ItemInterface[]{LEVEL1_GEMSTONE, LEVEL2_GEMSTONE, LEVEL3_GEMSTONE, LEVEL4_GEMSTONE, LEVEL5_GEMSTONE};
	}
}
