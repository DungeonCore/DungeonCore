package main.common.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Playerが強化を完了した瞬間
 * @author KENSUKE
 *
 */
public class PlayerStrengthFinishEvent extends PlayerEvent{
	private int chance;
	private ItemStack item;
	private int level;
	private boolean isSuccess;

	public PlayerStrengthFinishEvent(Player who, int chance, int level, ItemStack item, boolean isSuccess) {
		super(who);
		this.chance = chance;
		this.level = level;
		this.item = item;
		this.isSuccess = isSuccess;
	}

	public int getChance() {
		return chance;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getLevel() {
		return level;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
