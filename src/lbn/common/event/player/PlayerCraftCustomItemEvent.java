package lbn.common.event.player;

import lbn.api.player.TheLowPlayer;
import lbn.item.itemInterface.CraftItemable;
import lbn.item.system.craft.TheLowCraftRecipeInterface;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerCraftCustomItemEvent extends TheLowPlayerEvent {

	private CraftItemable itemInterface;
	private ItemStack craftedItem;

	public PlayerCraftCustomItemEvent(TheLowPlayer player, CraftItemable craftItemable, ItemStack craftedItem) {
		super(player);
		this.itemInterface = craftItemable;
		this.craftedItem = craftedItem;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * クラフトしたItemInterface
	 * 
	 * @return
	 */
	public CraftItemable getItemInterface() {
		return itemInterface;
	}

	/**
	 * クラフトに使用したレシピを取得
	 * 
	 * @return
	 */
	public TheLowCraftRecipeInterface getCraftRecipe() {
		return itemInterface.getCraftRecipe();
	}

	/**
	 * クラフトして実際に手に入れたアイテム
	 * 
	 * @return
	 */
	public ItemStack getCraftedItem() {
		return craftedItem;
	}
}
