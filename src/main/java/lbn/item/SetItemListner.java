package lbn.item;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import lbn.item.setItem.SetItemManager;
import lbn.item.setItem.SetItemPartsType;
import lbn.util.ItemStackUtil;

public class SetItemListner implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    if (((Player) e.getWhoClicked()).getGameMode() == GameMode.CREATIVE) { return; }

    // クリックした場所がSetItemの可能性がある場合
    if (SetItemPartsType.getTypeBySlot(e.getSlot()) != null && e.getClickedInventory().getType() == InventoryType.PLAYER) {
      // クリックしたアイテムがSETITEMのとき
      if (SetItemManager.isSetItem(e.getCurrentItem()) || SetItemManager.isSetItem(e.getCursor())) {
        // アイテムを置き終わった後のデータを見たいので２tick後に実行する
        new BukkitRunnable() {
          @Override
          public void run() {
            SetItemManager.updateAllSetItem((Player) e.getWhoClicked());
          }
        }.runTaskLater(Main.plugin, 2);
      }
    }
  }

  @EventHandler
  public void changeGameMode(PlayerGameModeChangeEvent e) {
    GameMode newGameMode = e.getNewGameMode();
    if (newGameMode != GameMode.CREATIVE) {
      SetItemManager.updateAllSetItem(e.getPlayer());
    }
  }

  @EventHandler
  public void join(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    SetItemManager.updateAllSetItem(player);
  }

  @EventHandler
  public void onPlayerItemBreakEvent(PlayerItemBreakEvent e) {
    // 壊れたアイテムがセットアイテムならチェックする
    ItemStack brokenItem = e.getBrokenItem();
    if (SetItemManager.isSetItem(brokenItem)) {
      SetItemManager.updateAllSetItem(e.getPlayer());
    }
  }

  @EventHandler
  public void quit(PlayerQuitEvent e) {
    SetItemManager.removeAll(e.getPlayer());
  }

  // TODO 効率悪いので後で修正する
  @EventHandler
  public void click(final PlayerInteractEvent e) {
    ItemStack item = e.getItem();
    if (ItemStackUtil.isEmpty(item)) { return; }
    boolean checkFlg = false;

    // クリックした部分のパーツが存在しない
    Collection<SetItemPartsType> partsTypeList = SetItemManager.getPartsTypeListByMaterial(item.getType());
    for (SetItemPartsType setItemPartsType : partsTypeList) {
      // もし装備するパーツでないならスキップする
      if (!setItemPartsType.isEquipParts()) {
        continue;
      }
      // 該当箇所にあるパーツを取得
      ItemStack equipItem = setItemPartsType.getItemStackByParts(e.getPlayer());
      // もし何もなければ装備したものとする
      if (ItemStackUtil.isEmpty(equipItem)) {
        checkFlg = true;
        break;
      }
    }

    if (!checkFlg) { return; }

    String setitemName = SetItemManager.getSetItemName(item);
    if (setitemName != null) {
      checkFlg = true;
    }

    if (checkFlg) {
      // 装備をきた後に処理を行う
      new BukkitRunnable() {
        @Override
        public void run() {
          SetItemManager.updateAllSetItem(e.getPlayer());
        }
      }.runTaskLater(Main.plugin, 1);
    }
  }

}
