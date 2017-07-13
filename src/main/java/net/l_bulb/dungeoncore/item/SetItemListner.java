package net.l_bulb.dungeoncore.item;

import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.l_bulb.dungeoncore.item.itemInterface.StatusItemable;
import net.l_bulb.dungeoncore.item.statusItem.SetItemManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class SetItemListner implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();

    // クリックしたのがPlayerのインベントリでないならなにもしない
    InventoryType type = e.getClickedInventory().getType();
    if (type != InventoryType.PLAYER) { return; }

    // クリックしたのがステータスアイテムでないなら何もしない
    if (ItemManager.getCustomItem(StatusItemable.class, e.getCurrentItem()) == null
        && ItemManager.getCustomItem(StatusItemable.class, e.getCursor()) == null) { return; }

    // ステータス変化処理を実行
    onClickItem(player);
  }

  @EventHandler
  public void join(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    SetItemManager.updateAllSetItem(player);
  }

  @EventHandler
  public void onPlayerItemBreakEvent(PlayerItemBreakEvent e) {
    // 壊れたアイテムがセットアイテムならチェックする
    if (ItemManager.getCustomItem(StatusItemable.class, e.getBrokenItem()) != null) {
      SetItemManager.updateAllSetItem(e.getPlayer());
    }
  }

  @EventHandler
  public void quit(PlayerQuitEvent e) {
    SetItemManager.removeAll(e.getPlayer());
  }

  @EventHandler
  public void click(final PlayerInteractEvent e) {
    // クリックしたアイテムがステータスアイテムでないなら何もしない
    StatusItemable customItem = ItemManager.getCustomItem(StatusItemable.class, e.getItem());
    if (customItem == null) { return; }
    // ステータス変化処理を実行
    onClickItem(e.getPlayer());
  }

  /**
   * アイテムをクリックした時の処理
   *
   * @param player
   */
  private void onClickItem(Player player) {
    // クリックする前の装備の数
    int beforeArmorCount = (int) Stream.of(player.getEquipment().getArmorContents()).filter(ItemStackUtil::isNotEmpty).count();

    TheLowExecutor.executeLater(2, () -> {
      // クリックした後の装備の数
      int afterArmorCount = (int) Stream.of(player.getEquipment().getArmorContents()).filter(ItemStackUtil::isNotEmpty).count();
      // 装備に変化があればステータスのチェック処理を行う
      if (beforeArmorCount != afterArmorCount) {
        SetItemManager.updateAllSetItem(player, true);
      }
    });
  }
}
