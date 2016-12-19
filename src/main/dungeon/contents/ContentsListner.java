package main.dungeon.contents;

import main.common.event.player.PlayerStrengthFinishEvent;
import main.dungeon.contents.item.shootbow.DebugBow;
import main.dungeon.contents.item.sword.DebugSword;
import main.item.ItemInterface;
import main.item.ItemManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ContentsListner implements Listener{
	@EventHandler
	public void onStrength(PlayerStrengthFinishEvent e) {
		ItemStack item = e.getItem();
		ItemInterface customItem = ItemManager.getCustomItem(item);
		if (customItem instanceof DebugSword) {
			DebugSword.availableLevel = e.getLevel();
		} else if (customItem instanceof DebugBow) {
			DebugBow.availableLevel = e.getLevel();
		}
	}
}
