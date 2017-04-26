package lbn.item.customItem.SpreadSheetItem;

import lbn.item.itemInterface.MaterialItemable;


public class SpreadSheetMaterialItem extends SpreadSheetOtherItem implements MaterialItemable{

	public SpreadSheetMaterialItem(String name, String id, int price, String command, String detail) {
		super(name, id, price, command, detail);
	}
}
