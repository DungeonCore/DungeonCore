package lbn.common.trade;

import io.netty.buffer.Unpooled;
import lbn.common.trade.nms.MerchantImplemention;
import lbn.common.trade.nms.MerchantRecipeListImplemention;
import net.minecraft.server.v1_8_R1.Container;
import net.minecraft.server.v1_8_R1.ContainerMerchant;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.InventoryMerchant;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;
import net.minecraft.server.v1_8_R1.PacketDataSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R1.PacketPlayOutOpenWindow;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;

public class TheLowTrades {
	/**
	 * 設定したTrade画面を開く
	 * @param merchant
	 * @param p
	 */
	public static void open(TheLowMerchant merchant, Player p) {
		openTrade(new MerchantImplemention(merchant), ((CraftPlayer) p).getHandle(), merchant);
	}

	@SuppressWarnings("unchecked")
	private static void openTrade(MerchantImplemention imerchant, EntityPlayer p, TheLowMerchant merchant) {
		Container container = CraftEventFactory.callInventoryOpenEvent(p, new ContainerMerchant(p.inventory, imerchant, p.world));
		if (container == null) {
			return;
		}

		imerchant.a_(p);

		int containerCounter = imerchant.getContainerCounter();

		//ココらへんはマイクラの処理のまま
		p.nextContainerCounter();
		p.activeContainer = container;
		p.activeContainer.windowId = containerCounter;
		p.activeContainer.addSlotListener(p);
		InventoryMerchant inventorymerchant = ((ContainerMerchant) p.activeContainer).e();
		IChatBaseComponent ichatbasecomponent = imerchant.getScoreboardDisplayName();

		p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSize()));

		//レシピを登録する
		MerchantRecipeList merchantrecipelist = new MerchantRecipeListImplemention(merchant);
		for (TheLowMerchantRecipe recipe : merchant.getInitRecipes()) {
			merchantrecipelist.add(recipe.toMerchantRecipe());
		}

		//レシピのパケットを送る
		PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
		packetdataserializer.writeInt(containerCounter);
		merchantrecipelist.a(packetdataserializer);
		p.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
	}
}
