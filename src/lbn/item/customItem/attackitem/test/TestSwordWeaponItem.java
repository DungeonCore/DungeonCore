package lbn.item.customItem.attackitem.test;

import lbn.item.ItemManager;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.itemAbstract.SwordItem;

public class TestSwordWeaponItem extends SwordItem{

	public TestSwordWeaponItem(SpreadSheetWeaponData data) {
		super(data);
		ItemManager.registItem(this);
	}

	@Override
	public boolean isShowItemList() {
		return false;
	}
}
