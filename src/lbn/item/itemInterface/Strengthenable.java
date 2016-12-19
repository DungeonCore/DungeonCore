package lbn.item.itemInterface;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;

public interface Strengthenable extends ItemInterface {
	public StrengthTemplate getStrengthTemplate();

	public String getItemName();

	public int getMaxStrengthCount();

	public String[] getStrengthDetail(int level);
}
