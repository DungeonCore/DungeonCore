package net.l_bulb.dungeoncore.item.itemInterface;

import java.util.Collection;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.player.ItemType;

public interface SpecialAttackItemable {
  public String getSpecialName();

  public Collection<ItemInterface> getAllItem();

  public int getRank();

  public String getId();

  public ItemType getAttackType();
}
