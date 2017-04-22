package lbn.item.slot.slot;

import lbn.item.slot.AbstractSlot;

public abstract class UnUseSlot extends AbstractSlot {
	@Override
	public boolean isShowItemList() {
		return false;
	}
}
