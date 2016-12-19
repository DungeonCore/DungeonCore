package main.common.event.player;

import main.item.attackitem.AttackItemStack;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCombatEntityEvent extends PlayerEvent{

	private static final HandlerList handlers = new HandlerList();

	LivingEntity entity;
	double damage;

	AttackItemStack attackItem;

	public PlayerCombatEntityEvent(Player who, LivingEntity entity, ItemStack item, double damage) {
		super(who);
		this.attackItem = AttackItemStack.getInstance(item);
		this.damage = damage;
		this.entity = entity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
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
