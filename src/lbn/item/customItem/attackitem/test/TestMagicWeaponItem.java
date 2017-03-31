package lbn.item.customItem.attackitem.test;

import lbn.item.ItemManager;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.itemAbstract.MagicItem;

public class TestMagicWeaponItem extends MagicItem{

	public TestMagicWeaponItem(SpreadSheetWeaponData data) {
		super(data);
		ItemManager.registItem(this);
	}
	@Override
	public boolean isShowItemList() {
		return false;
	}
}
