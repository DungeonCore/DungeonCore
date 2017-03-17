package lbn.common.trade;

import io.netty.buffer.Unpooled;

import java.util.List;

import net.minecraft.server.v1_8_R1.MerchantRecipeList;
import net.minecraft.server.v1_8_R1.PacketDataSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutCustomPayload;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public abstract class TheLowMerchant {
	Player p;
	int containerCounter;

	public TheLowMerchant(Player p, int containerCounter) {
		this.p = p;
		this.containerCounter = containerCounter;
	}

	public int getContainerCounter() {
		return containerCounter;
	}

	/**
	 * Playerがアイテムを動かした時
	 */
	public void onSetItem() {
		InventoryView openInventory = p.getOpenInventory();
		onSetItem(openInventory);
	}

	/**
	 * Playerがアイテムを動かした時の処理
	 * @param inv
	 */
	protected abstract void onSetItem(InventoryView inv);

	public Player getPlayer() {
		return p;
	}

	/**
	 * 表示される名前
	 *
	 * @return
	 */
	abstract public String getName();

	/**
	 * Result欄にアイテムが表示された時の処理
	 *
	 * @param recipe
	 */
	abstract public void onShowResult(TheLowMerchantRecipe recipe);

	/**
	 * レシピのパケットを送信する
	 * @param recipeList
	 */
	@SuppressWarnings("unchecked")
	protected void sendRecipeList(List<TheLowMerchantRecipe> recipeList) {
		// リストを作成
		MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
		for (TheLowMerchantRecipe recipe : recipeList) {
			merchantrecipelist.add(recipe);
		}

		//パケットを送信する
		PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
		packetdataserializer.writeInt(getContainerCounter());
		merchantrecipelist.a(packetdataserializer);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
	}
}
