package lbn.money;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.item.ItemManager;
import lbn.item.itemInterface.MoneyItemable;
import lbn.money.shop.CustomShop;
import lbn.money.shop.ShopItem;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

public class MoneyListener implements Listener {

  static HashMap<UUID, Long> consumMap = new HashMap<UUID, Long>();

  @EventHandler
  public void onClickShop(InventoryClickEvent e) {
    Inventory inventory = e.getInventory();
    String title = inventory.getTitle();
    if (inventory.getType() != InventoryType.CHEST || !title.endsWith("shop")) { return; }

    e.setCancelled(true);

    Player p = (Player) e.getWhoClicked();

    long consumeTime = consumMap.getOrDefault(p.getUniqueId(), 0L);
    if (consumeTime > System.currentTimeMillis()) { return; }
    consumMap.put(p.getUniqueId(), System.currentTimeMillis() + 100);

    // Playerデータがロードされていない時は何もしない
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      Message.sendMessage(p, ChatColor.RED + "現在Playerデータをロードしています。もう暫くお待ち下さい");
      return;
    }

    InventoryView view = e.getView();
    if (e.getClickedInventory() == null || e.getClickedInventory().equals(view.getBottomInventory())) { return; }

    if (ItemStackUtil.isEmpty(e.getCurrentItem())) { return; }

    CustomShop customShop = new CustomShop(title.replace(" shop", ""));

    ShopItem shopItem = customShop.fromShopItem(e.getCurrentItem(), true);
    if (shopItem == null) {
      Message.sendMessage(p, ChatColor.RED + "エラーが発生したためそのアイテムを購入できませんでした。");
      p.closeInventory();
      return;
    }

    // お金チェック
    if (theLowPlayer.getGalions() < shopItem.getPrice()) {
      Message.sendMessage(p, ChatColor.RED + "お金が足りないので購入できません。");
      return;
    }

    // インベントリチェック
    if (p.getInventory().firstEmpty() == -1) {
      Message.sendMessage(p, ChatColor.RED + "インベントリに空きがないので購入できません。");
      p.closeInventory();
      return;
    }

    // インベントリに追加する
    ItemStack buyItem = shopItem.getItem();
    buyItem.setAmount(shopItem.getCount());
    p.getInventory().addItem(buyItem);

    // お金の計算を行う
    theLowPlayer.addGalions(-shopItem.getPrice(), GalionEditReason.consume_shop);
  }

  @EventHandler
  public void onDragShop(InventoryDragEvent e) {
    Inventory inventory = e.getInventory();
    String title = inventory.getTitle();
    if (inventory.getType() != InventoryType.CHEST || !title.endsWith("shop")) { return; }
    e.setCancelled(true);
  }

  @EventHandler
  public void onDropMoney(PlayerDropItemEvent e) {
    MoneyItemable customItem = ItemManager.getCustomItem(MoneyItemable.class, e.getItemDrop().getItemStack());
    if (customItem != null) {
      // ドロップさせないで消す
      e.setCancelled(true);
      customItem.applyGalionItem(e.getPlayer());
    }
  }

  @EventHandler
  public void onPickupMoney(PlayerPickupItemEvent e) {
    MoneyItemable customItem = ItemManager.getCustomItem(MoneyItemable.class, e.getItem().getItemStack());
    if (customItem != null) {
      customItem.applyGalionItem(e.getPlayer());
    }
  }

  @EventHandler
  public void onClickMoney(InventoryClickEvent e) {
    MoneyItemable customItem = ItemManager.getCustomItem(MoneyItemable.class, e.getCurrentItem());
    if (customItem != null) {
      customItem.applyGalionItem((Player) e.getWhoClicked());
      return;
    }
    MoneyItemable customItem2 = ItemManager.getCustomItem(MoneyItemable.class, e.getCursor());
    if (customItem2 != null) {
      customItem2.applyGalionItem((Player) e.getWhoClicked());
      return;
    }
  }

  @EventHandler
  public void onDragMoney(InventoryDragEvent e) {
    MoneyItemable customItem = ItemManager.getCustomItem(MoneyItemable.class, e.getOldCursor());
    if (customItem != null) {
      customItem.applyGalionItem((Player) e.getWhoClicked());
      return;
    }
    MoneyItemable customItem2 = ItemManager.getCustomItem(MoneyItemable.class, e.getCursor());
    if (customItem2 != null) {
      customItem2.applyGalionItem((Player) e.getWhoClicked());
      return;
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    BuyerShopSelector.onSelect(e);
  }

  @EventHandler
  public void onDrag(InventoryDragEvent e) {
    BuyerShopSelector.onSelect(e);
  }
}
