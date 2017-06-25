package net.l_bulb.dungeoncore.dungeon.contents.item.key;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.itemInterface.InventoryClickItemable;
import net.l_bulb.dungeoncore.player.PlayerChecker;

public interface KeyItemable extends InventoryClickItemable, ItemInterface {
  public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item);

  public String getLastLine(Player p, String[] params);

  @Override
  default void onInventoryClick(InventoryInteractEvent event, ItemStack item, TheLowInventoryType type) {
    Player player = (Player) event.getWhoClicked();
    // 運営なら何もしない
    if (PlayerChecker.isNonNormalPlayer(player)) { return; }

    // 倉庫に鍵をいれられないようにする
    if (type == TheLowInventoryType.ENDER_CHEST || type == TheLowInventoryType.REPOSITORY) {
      event.setCancelled(true);
    }
  }
}
