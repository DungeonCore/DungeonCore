package main.item.SpreadSheetItem;

public class SpreadSheetQuestItem extends SpreadSheetOtherItem{

	public SpreadSheetQuestItem(String name, String id, int price,
			String command) {
		super(name, id, price, command);
	}

	@Override
	public boolean isQuestItem() {
		return true;
	}

}
