package net.l_bulb.dungeoncore.item.setItem;

import org.bukkit.Material;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface SetItemPartable extends ItemInterface {

  public SetItemInterface getBelongSetItem();

  public SetItemPartsType getItemSetPartsType();

  public Material getMaterial();
}
