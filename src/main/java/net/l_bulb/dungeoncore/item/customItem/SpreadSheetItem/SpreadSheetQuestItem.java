package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

public class SpreadSheetQuestItem extends SpreadSheetOtherItem {

  public SpreadSheetQuestItem(String name, String id, int price,
      String command, String detail, boolean remveWhenDeath) {
    super(name, id, price, command, detail, remveWhenDeath);
  }

  @Override
  public boolean isQuestItem() {
    return true;
  }

}
