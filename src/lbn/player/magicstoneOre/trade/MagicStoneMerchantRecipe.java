package lbn.player.magicstoneOre.trade;

import lbn.item.implementation.MagicStoneOre;
import lbn.player.magicstoneOre.MagicStoneOreType;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;

import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;

public class MagicStoneMerchantRecipe extends MerchantRecipe{

	//石炭
	static org.bukkit.inventory.ItemStack coal = MagicStoneOre.getMagicStoneOre(MagicStoneOreType.COAL_ORE).getItem();

	MagicStoneOreType type;

	public MagicStoneMerchantRecipe(MagicStoneOreType type, ItemStack paramItemStack3) {
		super(CraftItemStack.asNMSCopy(MagicStoneOre.getMagicStoneOre(type).getItem()),
				CraftItemStack.asNMSCopy(coal),
				paramItemStack3, 0, 500);
		this.type = type;
	}

	/**
	 * 魔法鉱石の種類を取得する
	 * @return
	 */
	public MagicStoneOreType getType() {
		return type;
	}

	@Override
	public boolean h() {
		return false;
	}

	@Override
	public void g() {
	}
}
