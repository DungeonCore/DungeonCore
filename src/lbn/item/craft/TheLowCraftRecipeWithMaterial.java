package lbn.item.craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.util.ItemStackUtil;

public class TheLowCraftRecipeWithMaterial implements TheLowCraftRecipeInterface {
	//ItemId
	HashMap<String, Integer> materialMap = new HashMap<String, Integer>();

	@Override
	public void addMaterial(String itemid, int amount) {
		if (itemid == null || itemid.isEmpty()) {
			return;
		}
		materialMap.put(itemid, amount);
	}

	@Override
	public Map<ItemInterface, Integer> getMaterialMap() {
		HashMap<ItemInterface, Integer> itemInterfaceMap = new HashMap<ItemInterface, Integer>();
		for (Entry<String, Integer> entry : materialMap.entrySet()) {
			//アイテムを取得する
			ItemInterface customItemById = ItemManager.getCustomItemById(entry.getKey());
			//もしアイテムが存在しないならクラフトできないとし、nullを返す
			if (customItemById == null) {
				return null;
			}
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
	public boolean hasAllMaterial(Player p) {
		PlayerInventory inventory = p.getInventory();
		for (Entry<String, Integer> entry : materialMap.entrySet()) {
			ItemStack itemStack = ItemStackUtil.getItemStack(entry.getKey());
			if (itemStack == null) {
				continue;
			}
			//1つでも持ってなかったらFALSE
			if (!inventory.contains(itemStack, entry.getValue())) {
				return false;
			}
		}
		return true;
	}
}
