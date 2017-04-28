package net.l_bulb.dungeoncore.item.customItem.attackitem.test;

import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.itemAbstract.BowItem;

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
