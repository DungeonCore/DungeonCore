package lbn.item.system.craft;

import java.util.HashMap;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.itemInterface.CraftItemable;
import lbn.item.system.craft.craftingViewer.CraftViewerForMainItemRecipe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TheLowCraftRecipeWithMainItem extends TheLowCraftRecipeWithMaterial {
  // ItemId
  String mainItemId;
  HashMap<String, Integer> materialMap = new HashMap<String, Integer>();

  public TheLowCraftRecipeWithMainItem(String mainItemId) {
    this.mainItemId = mainItemId;
  }

  @Override
  public ItemInterface getMainItem() {
    return ItemManager.getCustomItemById(mainItemId);
  }

  @Override
  public boolean hasMainItem() {
    return true;
  }

  @Override
  public boolean hasAllMaterial(Player p, boolean withMainItem) {
    if (!super.hasAllMaterial(p, withMainItem)) { return false; }
    return !withMainItem || contains(p.getInventory(), getMainItem());
  }

  /**
   * 指定されたアイテムが指定されたインベントリに入っていたらTRUE
   * 
   * @param inv
   * @param item
   * @return
   */
  private boolean contains(Inventory inv, ItemInterface item) {
    if (item == null) { return false; }
    for (ItemStack i : inv.getContents()) {
      if (item.isThisItem(i)) { return true; }
    }
    return false;
  }

  @Override
  public void openCraftingViewer(Player p, CraftItemable craftingItem) {
    CraftViewerForMainItemRecipe.open(p, craftingItem);
  }
}
