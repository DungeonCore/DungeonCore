package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.cooltime.Cooltimable;

public interface MagicExcuteable extends Cooltimable {
  ItemStack getItem();

  int getNeedMagicPoint();

  boolean isShowMessageIfUnderCooltime();

  void excuteMagic(Player p, PlayerInteractEvent e);
}
