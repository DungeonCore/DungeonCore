package lbn.dungeon.contents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.item.shootbow.DebugBow;
import lbn.dungeon.contents.item.sword.DebugSword;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;

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
