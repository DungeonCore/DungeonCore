package net.l_bulb.dungeoncore.item.customItem.attackitem.test;

import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.itemAbstract.SwordItem;

public class TestSwordWeaponItem extends SwordItem {

  public TestSwordWeaponItem(SpreadSheetWeaponData data) {
    super(data);
    ItemManager.registItem(this);
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }
}
