package lbn.money.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.NbtTagConst;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.itemInterface.AvailableLevelItemable;
import lbn.util.ItemStackUtil;
import lbn.util.Message;

public class ShopItem {
  public ShopItem(ItemStack item, int price, int count) {
    this.item = item;
    this.price = price;
    this.count = Math.min(count, 64);
  }

  public ShopItem(ItemStack item, int price) {
    this(item, price, item.getAmount());
  }

  public static ShopItem getBlank() {
    return new ShopItem(new ItemStack(Material.AIR), 0);
  }

  ItemStack dispItem;
  ItemStack item;
  int price;
  int count;

  /**
   * 商品としてお店に並べるアイテム
   * 
   * @return
   */
  public ItemStack getShopDispItem() {
    if (item.getType() == Material.AIR) { return item; }

    if (dispItem != null) { return dispItem; }

    dispItem = item.clone();
    ArrayList<String> lore = new ArrayList<String>();
    // IDがあればプラグイン用のアイテムなのでIDを記載する
    String id = ItemStackUtil.getId(item);
    if (id != null) {
      // IDを付与
      ItemStackUtil.setNBTTag(dispItem, NbtTagConst.THELOW_ITEM_ID, id);
      lore.add(ChatColor.BLACK + ItemStackUtil.getLoreForIdLine(id));

      // IDがなければプラグイン用ではないのでloreをそのまま記載する
    } else {
      List<String> lore2 = ItemStackUtil.getLore(item);
      lore.addAll(lore2);
    }
    lore.add(ChatColor.GOLD + Message.getMessage("Price : {0} Galions", price));
    lore.add(ChatColor.GOLD + Message.getMessage("Quantity : {0}", count));
    lore.add(ChatColor.GRAY + Message.getMessage("使用可能レベル : {0}", getAvailableLevel(item)));
    ItemStackUtil.setLore(dispItem, lore);
    dispItem.setAmount(count);
    return dispItem;
  }

  protected static String getAvailableLevel(ItemStack item2) {
    ItemInterface customItem = ItemManager.getCustomItem(item2);
    if (customItem == null || !(customItem instanceof AvailableLevelItemable)) { return "制限なし"; }

    AvailableLevelItemable item = (AvailableLevelItemable) customItem;
    return item.getLevelType().getName() + " " + item.getAvailableLevel() + "以上";
  }

  /**
   * 実際に購入者に渡すアイテム
   * 
   * @return
   */
  public ItemStack getItem() {
    return item;
  }

  public int getPrice() {
    return price;
  }

  public int getCount() {
    return count;
  }

}
