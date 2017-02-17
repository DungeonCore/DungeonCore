package lbn.item;

import java.util.List;

import lbn.item.slot.SlotInterface;

public class ItemLoreSetter {
	protected AbstractItem item;

	/**
	 * 一般のアイテムに対してのLoreをセットする
	 * @param item
	 */
	public ItemLoreSetter(AbstractItem item) {
		this.item = item;
	}

	//強化レベルをセットする
	int strengthLevel = 0;

	/**
	 * 強化レベルをセットする
	 * @param level
	 */
	public void setStrengthLevel(int level) {
		strengthLevel = level;
	}

	List<SlotInterface> slotList = null;

	/**
	 * 装着するSlotをセットする
	 * @param slotList
	 */
	public void setSlotList(List<SlotInterface> slotList) {
		this.slotList = slotList;
	}

	/**
	 * Loreを取得する
	 * @return
	 */
	protected List<String> getLore(){
		return null;
	}
}
