package net.l_bulb.dungeoncore.item.system.craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CraftItemable;
import net.l_bulb.dungeoncore.item.system.craft.craftingViewer.CraftViewerForOnlyMaterialRecipe;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class TheLowCraftRecipeWithMaterial implements TheLowCraftRecipeInterface {
  // ItemId
  HashMap<String, Integer> materialMap = new HashMap<String, Integer>();

  @Override
  public void addMaterial(String itemid, int amount) {
    if (itemid == null || itemid.isEmpty()) { return; }
    materialMap.put(itemid, amount);
  }

  @Override
  public Map<ItemInterface, Integer> getMaterialMap() {
    HashMap<ItemInterface, Integer> itemInterfaceMap = new HashMap<ItemInterface, Integer>();
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

  @Override
  public void openCraftingViewer(Player p, CraftItemable craftingItem) {
    CraftViewerForOnlyMaterialRecipe.open(p, craftingItem);
  }
}
