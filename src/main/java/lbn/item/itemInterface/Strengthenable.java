package lbn.item.itemInterface;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.item.system.lore.ItemLoreToken;

public interface Strengthenable extends ItemInterface {
  public StrengthTemplate getStrengthTemplate();

  public String getItemName();

  public int getMaxStrengthCount();

  public void setStrengthDetail(int level, ItemLoreToken loreToken);
}
