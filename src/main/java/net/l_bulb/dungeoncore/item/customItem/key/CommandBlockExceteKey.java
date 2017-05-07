package net.l_bulb.dungeoncore.item.customItem.key;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeon.contents.item.key.AbstractKeyItem;
import net.l_bulb.dungeoncore.dungeoncore.Main;

public abstract class CommandBlockExceteKey extends AbstractKeyItem {

  @Override
  public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item) {
    Location location = e.getClickedBlock().getLocation();
    Player p = e.getPlayer();

    Location add = location.add(0, -2, 0);
    if (add.getBlock().getType() != Material.COMMAND) {
      sendError(p, "看板の２つ下にコマンドブロックが存在しません。");
      return;
    }

    Location add2 = add.add(0, -2, 0);
    if (add2.getBlock().isEmpty() || add2.getBlock().isLiquid()) {
      sendError(p, "コマブロの２つしたにブロックが存在しません。");
      return;
    }

    final Location add3 = add2.add(0, 1, 0);
    add3.getBlock().setType(Material.REDSTONE_TORCH_ON);

    // アイテムを減らせる
    ItemStack itemInHand = p.getItemInHand();
    if (itemInHand.getAmount() == 1) {
      p.setItemInHand(new ItemStack(Material.AIR));
    } else {
      itemInHand.setAmount(itemInHand.getAmount() - 1);
      p.setItemInHand(itemInHand);
    }

    new BukkitRunnable() {
      @Override
      public void run() {
        add3.getBlock().setType(Material.BEDROCK);
      }
    }.runTaskLater(Main.plugin, 1);
  }

  private void sendError(Player player, String string) {
    if (player.isOp()) {
      player.sendMessage(ChatColor.RED + string);
    }
  }

  @Override
  public String getLastLine(Player p, String[] params) {
    return "";
  }

}
