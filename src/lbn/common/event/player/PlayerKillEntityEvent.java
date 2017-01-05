package lbn.common.event.player;

import lbn.item.attackitem.AttackItemStack;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerKillEntityEvent extends PlayerEvent{
	private static final HandlerList handlers = new HandlerList();

	LivingEntity entity;

	AttackItemStack attackItem;

	public PlayerKillEntityEvent(Player who, LivingEntity entity, ItemStack item) {
		super(who);
		this.attackItem = AttackItemStack.getInstance(item);
		this.entity = entity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public AttackItemStack getAttackItem() {
		return attackItem;
	}

	public LivingEntity getEnemy() {
		return entity;
	}

	public void callEvent() {
		if (attackItem != null) {
			Bukkit.getServer().getPluginManager().callEvent(this);
		}
	}
}
