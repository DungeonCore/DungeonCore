package net.l_bulb.dungeoncore.item.itemInterface;

import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;

public interface Strengthenable extends ItemInterface {
  public StrengthTemplate getStrengthTemplate();

  @Override
  public String getItemName();

  public int getMaxStrengthCount();

  public void setStrengthDetail(int level, ItemLoreToken loreToken);
}
