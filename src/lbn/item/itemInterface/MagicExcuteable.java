package lbn.item.itemInterface;

import lbn.item.Cooltimable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public interface MagicExcuteable extends Cooltimable{
	ItemStack getItem();

	int getNeedMagicPoint();

	boolean isShowMessageIfUnderCooltime();

	void excuteMagic(Player p, PlayerInteractEvent e);
}
