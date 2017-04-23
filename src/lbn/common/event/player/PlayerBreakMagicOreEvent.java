package lbn.common.event.player;

import lbn.player.magicstoneOre.MagicStoneOreType;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBreakMagicOreEvent extends PlayerEvent implements Cancellable{

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	//破壊したブロックの座標
	 Location loc;
	 //破壊した魔法鉱石のタイプ
	 MagicStoneOreType brokenType;
	 //壊すときに使ったアイテム
	 ItemStack itemInhand;
	 //壊して取得できるアイテム
	 ItemStack acquisition ;

	 /**
	  *
	  * @param who
	  * @param loc 破壊した魔法鉱石の座標
	  * @param brokenType 破壊した魔法鉱石のタイプ
	 * @param acquisition 取得できるアイテム
	  */
	public PlayerBreakMagicOreEvent(Player who, Location loc, MagicStoneOreType brokenType, ItemStack acquisition) {
		super(who);
		this.loc = loc;
		this.brokenType = brokenType;
		itemInhand = who.getItemInHand();
		this.acquisition = acquisition;

	}

	/**
	 * 壊すときに使ったアイテムを取得
	 * @return
	 */
	public ItemStack getUseItem() {
		return itemInhand;
	}

	/**
	 * 破壊した魔法鉱石のタイプを取得
	 * @return
	 */
	public MagicStoneOreType getBrokenType() {
		return brokenType;
	}

	/**
	 * 取得できるアイテムを取得
	 * @return
	 */
	public ItemStack getAcquisition() {
		return acquisition;
	}

	/**
	 * 取得できるアイテムをセット
	 * @param acquisition
	 */
	public void setAcquisition(ItemStack acquisition) {
		this.acquisition = acquisition;
	}

	/**
	 * 破壊した魔法鉱石の座標を取得
	 * @return
	 */
	public Location getLocation() {
		return loc;
	}

	boolean isCancel = false;

	@Override
	public boolean isCancelled() {
		return isCancel;
	}

	@Override
	public void setCancelled(boolean paramBoolean) {
		isCancel = paramBoolean;
	}

}
