package lbn.item.craft;

import java.util.HashMap;
import java.util.Map.Entry;

import lbn.item.ItemInterface;
import lbn.item.ItemLoreToken;

import org.bukkit.ChatColor;

public class TheLowCraftRecipeWithMainItem implements TheLowCraftRecipeInterface {
	//ItemId
	String itemId;
	HashMap<String, Integer> materialMap = new HashMap<String, Integer>();

	//ItemInterface
	ItemInterface centerItem = null;
	HashMap<String, Integer> materialNameMap = new HashMap<String, Integer>();

	//材料を表示するLore
	ItemLoreToken itemLoreToken = new ItemLoreToken("材料");

	public TheLowCraftRecipeWithMainItem(String mainItemId) {
		this.itemId = mainItemId;
	}

	@Override
	public void addMaterial(String itemid, int amount) {
		materialMap.put(itemid, amount);
	}

	/**
	 * updateをする
	 */
	protected void updateItem() {
		itemLoreToken.addLore(centerItem.getItemName(), ChatColor.GOLD);
		for (Entry<String, Integer> entry : materialNameMap.entrySet()) {
			itemLoreToken.addLore(entry + "   " + entry.getValue() + "個");
		}
	}

	@Override
	public ItemLoreToken getViewLore() {
		//TODO cache化する
		updateItem();
		return itemLoreToken;
	}

}
