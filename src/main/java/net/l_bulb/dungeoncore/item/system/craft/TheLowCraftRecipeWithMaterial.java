package net.l_bulb.dungeoncore.item.system.craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class TheLowCraftRecipeWithMaterial implements TheLowCraftRecipeInterface {
  // ItemId
  HashMap<String, Integer> materialMap = new HashMap<>();

  @Override
  public void addMaterial(String itemid, int amount) {
    if (itemid == null || itemid.isEmpty()) { return; }
    materialMap.put(itemid, amount);
  }

  @Override
  public Map<ItemInterface, Integer> getMaterialMap() {
    HashMap<ItemInterface, Integer> itemInterfaceMap = new HashMap<>();
    for (Entry<String, Integer> entry : materialMap.entrySet()) {
      // アイテムを取得する
      ItemInterface customItemById = ItemManager.getCustomItemById(entry.getKey());
      // もしアイテムが存在しないならクラフトできないとし、nullを返す
      if (customItemById == null) { return null; }
      itemInterfaceMap.put(customItemById, entry.getValue());
    }
    return itemInterfaceMap;
  }

  @Override
  public ItemInterface getMainItem() {
    return null;
  }

  @Override
  public boolean hasMainItem() {
    return false;
  }

  @Override
  public boolean hasAllMaterial(Player p, boolean withMainItem) {
    PlayerInventory inventory = p.getInventory();
    for (Entry<String, Integer> entry : materialMap.entrySet()) {
      if (entry.getKey() == null) {
        continue;
      }
      // 1つでも持ってなかったらFALSE
      if (!ItemStackUtil.containsCustomItem(inventory, entry.getKey(), entry.getValue())) { return false; }
    }
    return true;
  }

  @Override
  public void removeMaterial(Inventory inv) {
    for (Entry<ItemInterface, Integer> entry : getMaterialMap().entrySet()) {
      ItemStackUtil.removeCustomItem(inv, entry.getKey().getId(), entry.getValue());
    }
  }

  // 成功時のアイテムID
  String successItemId;

  // 大成功時のアイテムID
  String greateSuccessItemId;

  @Override
  public ItemInterface getSuccessItem() {
    return ItemManager.getCustomItemById(successItemId);
  }

  @Override
  public void setSuccessItemId(String successItemId) {
    this.successItemId = successItemId;
  }

  @Override
  public ItemInterface getGreateSuccessItem() {
    return ItemManager.getCustomItemById(greateSuccessItemId);
  }

  @Override
  public void setGreateSuccessItemId(String greateSuccessItemId) {
    this.greateSuccessItemId = greateSuccessItemId;
  }

  String craftItemId;

  @Override
  public ItemInterface getCraftItem() {
    return ItemManager.getCustomItemById(craftItemId);
  }

  @Override
  public void setCraftItemId(String id) {
    this.craftItemId = id;
  }

  @Override
  public boolean isValidRecipe() {
    // クラフトアイテムが存在しないならFALSE
    if (getCraftItem() == null) { return false; }

    // 成功アイテムが設定されていて、そのIDが存在しないならクラフトできない
    if (successItemId != null) {
      if (getSuccessItem() == null) { return false; }
    }

    // 成功アイテムが設定されていて、そのIDが存在しないならクラフトできない
    if (greateSuccessItemId != null) {
      if (getGreateSuccessItem() == null) { return false; }
    }

    return true;
  }

  String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

}
