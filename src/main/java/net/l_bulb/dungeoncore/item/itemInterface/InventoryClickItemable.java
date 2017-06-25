package net.l_bulb.dungeoncore.item.itemInterface;

import java.util.Objects;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface InventoryClickItemable extends ItemInterface {
  /**
   * インベントリをクリックした時の処理
   *
   * @param event
   * @param item クリックしたアイテム
   * @param type インベントリのタイプ
   */
  void onInventoryClick(InventoryInteractEvent event, ItemStack item, TheLowInventoryType type);

  /**
   * インベントリのタイプを取得する
   *
   * @param inv
   * @return
   */
  public static TheLowInventoryType getInventoryType(Inventory inv) {
    InventoryType type = inv.getType();
    if (type == InventoryType.ENDER_CHEST) { return TheLowInventoryType.ENDER_CHEST; }

    // ラージチェストなら倉庫と判断
    String name = inv.getName();
    if (Objects.equals(name, "Large chest") && inv.getSize() == 9 * 6) { return TheLowInventoryType.REPOSITORY; }

    return TheLowInventoryType.NORMAL_CHEST;
  }

  public enum TheLowInventoryType {
    NORMAL_CHEST, ENDER_CHEST, REPOSITORY;
  }
}
