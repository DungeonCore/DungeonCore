package lbn.item.customItem.SpreadSheetItem;

public class SpreadSheetQuestItem extends SpreadSheetOtherItem {

  public SpreadSheetQuestItem(String name, String id, int price,
      String command, String detail) {
    super(name, id, price, command, detail);
  }

  @Override
  public boolean isQuestItem() {
    return true;
  }

}
