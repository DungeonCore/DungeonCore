package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import net.l_bulb.dungeoncore.item.itemInterface.MaterialItemable;

public class SpreadSheetMaterialItem extends SpreadSheetOtherItem implements MaterialItemable {

  public SpreadSheetMaterialItem(String name, String id, int price, String command, String detail, boolean remveWhenDeath) {
    super(name, id, price, command, detail, remveWhenDeath);
  }
}
