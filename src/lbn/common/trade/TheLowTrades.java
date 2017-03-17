package lbn.common.trade;

import java.lang.reflect.Constructor;

import lbn.common.trade.nms.MerchantImplemention;
import lbn.util.JavaUtil;
import net.minecraft.server.v1_8_R1.Container;
import net.minecraft.server.v1_8_R1.ContainerMerchant;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.InventoryMerchant;
import net.minecraft.server.v1_8_R1.PacketPlayOutOpenWindow;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;

public class TheLowTrades {
	public static <T extends MerchantImplemention> void open(Player arg0, Class<T> merchantClass) {
		try {
			EntityPlayer p = ((CraftPlayer) arg0).getHandle();
			int containerCounter = JavaUtil.getField(Integer.class, "containerCounter", p);
			Constructor<T> imerchant;
			imerchant = merchantClass.getConstructor(Integer.class);
			T newInstance = imerchant.newInstance(containerCounter);
			newInstance.a_(p);
			openTrade(newInstance, p);
		} catch (Exception e) {
			e.printStackTrace();
			// 起こりえないので大丈夫
		}
	}

	public static void openTrade(MerchantImplemention imerchant, EntityPlayer p) {
		Container container = CraftEventFactory.callInventoryOpenEvent(p, new ContainerMerchant(p.inventory, imerchant, p.world));
		if (container == null) {
			return;
		}

		int containerCounter = imerchant.getContainerCounter();

		//ココらへんはマイクラの処理のまま
		p.nextContainerCounter();
		p.activeContainer = container;
		p.activeContainer.windowId = containerCounter;
		p.activeContainer.addSlotListener(p);
		InventoryMerchant inventorymerchant = ((ContainerMerchant) p.activeContainer).e();
		IChatBaseComponent ichatbasecomponent = imerchant.getScoreboardDisplayName();

		p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSize()));

	}
}
