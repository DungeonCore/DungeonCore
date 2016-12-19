package main.item.itemInterface;

import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.ItemInterface;

public interface Strengthenable extends ItemInterface {
	public StrengthTemplate getStrengthTemplate();

	public String getItemName();

	public int getMaxStrengthCount();

	public String[] getStrengthDetail(int level);
}
