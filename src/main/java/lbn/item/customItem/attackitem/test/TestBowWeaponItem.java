package lbn.item.customItem.attackitem.test;

import lbn.item.ItemManager;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.itemAbstract.BowItem;

public class TestBowWeaponItem extends BowItem {

  public TestBowWeaponItem(SpreadSheetWeaponData data) {
    super(data);
    ItemManager.registItem(this);
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }
}
