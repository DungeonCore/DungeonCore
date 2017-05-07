package net.l_bulb.dungeoncore.common.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuSelector implements MenuSelectorInterface {
  public void regist() {
    MenuSelectorManager.regist(this);
  }

  protected String title;
  protected Inventory createInventory;

  public MenuSelector(String title) {
    this.title = ChatColor.WHITE + title;
    createInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + title);
  }

  protected HashMap<ItemStack, SelectRunnable> runMap = new HashMap<>();

  /**
   * メニューを追加する
   * 
   * @param item
   * @param index
   * @param run
   * @return
   */
  public MenuSelector addMenu(ItemStack item, int index, SelectRunnable run) {
    createInventory.setItem(index, item);
    runMap.put(item, run);
    return this;
  }

  /**
   * メニューを開く
   * 
   * @param p
   */
  @Override
  public void open(Player p) {
    p.openInventory(createInventory);
  }

  /**
   * アイテムを選択したときの処理
   * 
   * @param p
   * @param item
   */
  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    if (runMap.containsKey(item)) {
      runMap.get(item).run(p, item);
    }
  }

  @Override
  public String getTitle() {
    return title;
  }
}
