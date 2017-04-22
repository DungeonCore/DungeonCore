package lbn.item.setItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractCommonSetItem implements SetItemInterface {

	@Override
	public boolean isWearSetItem(Player p) {
		// 装備が必要な部分を全てチェックする
		for (Entry<SetItemPartsType, SetItemPartable> entry : getFullSetItem().entrySet()) {
			ItemStack itemStackByParts = entry.getKey().getItemStackByParts(p);
			// もし装備していなければFALSE
			if (itemStackByParts == null) {
				return false;
			}
			// 別なものを装備していればFALSE
			if (!entry.getValue().isThisItem(itemStackByParts)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack[] getWearedSetItem(Player p) {
		Set<SetItemPartsType> keySet = getFullSetItem().keySet();
		ItemStack[] itemList = new ItemStack[keySet.size()];

		int count = 0;
		for (SetItemPartsType setItemPartsType : keySet) {
			itemList[count] = setItemPartsType.getItemStackByParts(p);
			count++;
		}
		return itemList;
	}

	@Override
	public SetItemPartable getSetItem(SetItemPartsType parts) {
		return getFullSetItem().get(parts);
	}

	@Override
	public boolean equals(Object paramObject) {
		if (paramObject instanceof SetItemInterface) {
			return getName().equalsIgnoreCase(((SetItemInterface) paramObject).getName());
		}
		return false;
	}

	/**
	 * 指定したパーツの部分が指定したitemと一致したらTRUE
	 * 
	 * @param item
	 * @param parts
	 * @return
	 */
	protected boolean isSetItem(ItemStack item, SetItemPartsType parts) {
		SetItemPartable setItem = getSetItem(parts);
		if (setItem == null) {
			return false;
		}
		return setItem.isThisItem(item);
	}

	@Override
	public int hashCode() {
		return getName().toUpperCase().hashCode();
	}

	protected abstract List<SetItemPartable> getAllItemParts();

	HashMap<SetItemPartsType, SetItemPartable> itemPartsMap = null;

	@Override
	public HashMap<SetItemPartsType, SetItemPartable> getFullSetItem() {
		// もしまだ生成されてないならMapを生成する
		if (itemPartsMap == null) {
			itemPartsMap = new HashMap<SetItemPartsType, SetItemPartable>();
			for (SetItemPartable setItemParts : getAllItemParts()) {
				itemPartsMap.put(setItemParts.getItemSetPartsType(), setItemParts);
			}
		}
		return itemPartsMap;
	}

}
