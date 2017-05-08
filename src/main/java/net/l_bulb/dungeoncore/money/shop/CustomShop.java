package net.l_bulb.dungeoncore.money.shop;

import java.util.ArrayList;

import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.AbstractSheetRunable;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpc;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpcManager;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class CustomShop extends Shop {
  public CustomShop(String villagerID) {
    // dataからLocationを取得しショップを作成する
    VillagerNpc villager = VillagerNpcManager.getVillagerNpcById(villagerID);
    String data = villager.getData();
    Location locationByString = AbstractSheetRunable.getLocationByString(data);
    this.loc = locationByString;
    this.name = villagerID;
  }

  @Override
  public String getName() {
    return name;
  }

  String name;
  Location loc = null;

  @Override
  protected ArrayList<ShopItem> getShopItemList() {
    if (loc == null) { return null; }

    Block block = loc.getBlock();
    BlockState state = block.getState();

    if (!(state instanceof Chest)) { return super.getShopItemList(); }

    ArrayList<ShopItem> shopitems = new ArrayList<ShopItem>();

    Chest chest = (Chest) state;
    for (ItemStack shopItemTemplate : chest.getBlockInventory().getContents()) {
      ShopItem fromShopItem = CustomShopItem.fromTemplate(shopItemTemplate);
      if (fromShopItem == null) {
        shopitems.add(ShopItem.getBlank());
      } else {
        shopitems.add(fromShopItem);
      }
    }
    return shopitems;
  }

  @Override
  public ShopItem fromShopItem(ItemStack shopItem, boolean error) {
    // shopid 取得
    String shopItemId = CustomShopItem.getShopItemId(shopItem);
    // IDがなけばれ通常のshopと同じ扱いにする
    if (shopItemId == null) {
      // new LbnRuntimeException("invaild shop item id :" + ItemStackUtil.getLore(shopItem) + ", shop name:" + getName()).printStackTrace();
      return super.fromShopItem(shopItem, error);
    }

    ItemStack templateByShopId = getTemplateByShopId(shopItemId);
    if (templateByShopId == null) {
      new LbnRuntimeException("can not found template item. id:" + shopItemId + ", shop name:" + getName()).printStackTrace();
      return super.fromShopItem(shopItem, error);
    }
    return CustomShopItem.fromTemplate(templateByShopId);
  }

  public ItemStack getTemplateByShopId(String shopItemId) {
    if (loc == null) { return null; }

    Block block = loc.getBlock();
    BlockState state = block.getState();

    if (!(state instanceof Chest)) { return null; }
    Chest chest = (Chest) state;
    for (ItemStack shopItemTemplate : chest.getBlockInventory().getContents()) {
      String shopItemIdForTemplate = CustomShopItem.getShopItemId(shopItemTemplate);
      if (shopItemIdForTemplate == null) {
        continue;
      }

      if (shopItemIdForTemplate.equals(shopItemId)) { return shopItemTemplate; }
    }
    return null;
  }
}
