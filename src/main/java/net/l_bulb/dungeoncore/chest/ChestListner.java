package net.l_bulb.dungeoncore.chest;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.LimitedListener;
import net.l_bulb.dungeoncore.chest.wireless.RepositoryMenu;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.ChestSheetRunnable;
import net.l_bulb.dungeoncore.util.Message;
import net.l_bulb.dungeoncore.util.TheLowUtil;

public class ChestListner implements Listener {

  @EventHandler
  public void onclick2(PlayerInteractEvent e) {
    Block clickedBlock = e.getClickedBlock();
    // エンダーチェストのとき
    if (clickedBlock != null && clickedBlock.getType() == Material.ENDER_CHEST) {
      if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        e.getPlayer().playSound(clickedBlock.getLocation(), Sound.CHEST_OPEN, 1, 1);
        // 倉庫を開く
        new RepositoryMenu().open(e.getPlayer());
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onclick(PlayerInteractEvent e) {
    if (!LimitedListener.isTarget(e.getPlayer().getWorld())) { return; }

    Player p = e.getPlayer();
    Action action = e.getAction();
    if (action == Action.RIGHT_CLICK_BLOCK) {
      if (e.getClickedBlock().getType() != Material.CHEST) { return; }

      Location loc = e.getClickedBlock().getLocation();
      AbstractCustomChest chest = CustomChestManager.getCustomChest(loc);

      if (chest == null) {
        if (p.getGameMode() != GameMode.CREATIVE) {
          e.setCancelled(true);
          Message.sendMessage(e.getPlayer(), "Oops!");

          if (ChestSheetRunnable.complateRead) {
            removeOopsChest(p, e.getClickedBlock());
          }
        }
        return;
      }

      // クリエ かつ シフトの時はそのまま中身を開く
      if (p.isSneaking() && p.getGameMode() == GameMode.CREATIVE) {
        chest.executeIfDebug(p, e.getClickedBlock(), e);
        return;
      }

      e.setCancelled(true);
      if (chest.canOpen(p, e.getClickedBlock(), e)) {
        chest.open(p, e.getClickedBlock(), e);
      }
    } else if (action == Action.LEFT_CLICK_BLOCK) {
      if (e.getClickedBlock().getType() != Material.CHEST) { return; }
      Location loc = e.getClickedBlock().getLocation();
      AbstractCustomChest chest = CustomChestManager.getCustomChest(loc);

      if (chest == null || !(chest instanceof SpletSheetChest)) { return; }

      e.setCancelled(true);
      if (p.getGameMode() == GameMode.CREATIVE || p.isOp()) {
        String name = ChestLocationManager.getName(((SpletSheetChest) chest).contentLoc);
        p.sendMessage("");
        p.sendMessage(ChatColor.GREEN + "中身:" + (name == null ? "オリジナル" : name));
        p.sendMessage(ChatColor.GREEN + "reful:" + (int) (((SpletSheetChest) chest).refuelTick / 20.0) + "秒,  type:"
            + (chest instanceof AllPlayerSameContentChest) + ",  max:" + ((SpletSheetChest) chest).maxItemCount + ",  min:"
            + ((SpletSheetChest) chest).minItemCount);
      }
    }
  }

  private void removeOopsChest(Player p, Block clickedBlock) {
    Chest state = (Chest) clickedBlock.getState();
    Inventory blockInventory = state.getBlockInventory();
    ItemStack[] contents = blockInventory.getContents();

    boolean containsItem = false;
    for (ItemStack itemStack : contents) {
      if (itemStack != null) {
        containsItem = true;
        continue;
      }
    }

    if (!containsItem) {
      clickedBlock.setType(Material.AIR);
      TheLowUtil.addBonusGold(p, clickedBlock.getLocation());
    }
  }
}
