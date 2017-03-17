package lbn.item.craft;

import java.util.HashMap;
import java.util.Map.Entry;

import lbn.item.ItemLoreToken;

public class TheLowCraftRecipeWithMaterial implements TheLowCraftRecipeInterface {
	//ItemId
	HashMap<String, Integer> materialMap = new HashMap<String, Integer>();

	//ItemInterface
	HashMap<String, Integer> materialNameMap = new HashMap<String, Integer>();

	//材料を表示するLore
	ItemLoreToken itemLoreToken = new ItemLoreToken("材料");

//	public TheLowCraftRecipe(String mainItemId) {
//		this.itemId = mainItemId;
//	}

	@Override
	public void addMaterial(String itemid, int amount) {
		materialMap.put(itemid, amount);
	}


	/**
	 * updateをする
	 */
	protected void updateItem() {
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
