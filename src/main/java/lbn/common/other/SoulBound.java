package lbn.common.other;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lbn.util.TheLowUtil;

public class SoulBound {
  public static final String EXTRA_INV_NAME = "extra inventory";

  /**
   * 素材がsoulbound付きの場合はsoulboundをつける
   * 
   * @param e
   */
  public static void onCraftSoulBound(PrepareItemCraftEvent e) {
    CraftingInventory inventory = e.getInventory();
    ItemStack[] matrix = inventory.getMatrix();

    boolean isSoulBound = false;
    for (ItemStack itemStack : matrix) {
      if (TheLowUtil.isSoulBound(itemStack)) {
        isSoulBound = true;
      }
    }

    if (isSoulBound) {
      ItemStack result = inventory.getResult();
      TheLowUtil.addSoulBound(result);
      inventory.setResult(result);

      for (HumanEntity p : e.getViewers()) {
        if (p instanceof Player) {
          ((Player) p).updateInventory();
        }
      }
    }
  }

  public static void onInventoryClick(InventoryClickEvent event) {
    HumanEntity entity = event.getWhoClicked();

    if (!(entity instanceof Player)) { return; }
    ItemStack stack = entity.getItemOnCursor();
    Inventory topInventory = event.getView().getTopInventory();
    InventoryType top = topInventory.getType();

    if (stack != null) {
      if ((top == InventoryType.PLAYER) || (top == InventoryType.WORKBENCH) || (top == InventoryType.CRAFTING)) { return; }

      if (top == InventoryType.CHEST && EXTRA_INV_NAME.equals(topInventory.getTitle())) { return; }
      if (top == InventoryType.ENDER_CHEST) { return; }

      if (TheLowUtil.isSoulBound(stack)) {
        event.setCancelled(true);
      }
    }
  }

  public static void onDrops(PlayerDropItemEvent event) {
    Item itemDrop = event.getItemDrop();
    if (itemDrop == null) { return; }

    ItemStack itemStack = itemDrop.getItemStack();
    if (itemStack == null) { return; }

    if (TheLowUtil.isSoulBound(itemStack)) {
      itemDrop.remove();
    }
  }
}
