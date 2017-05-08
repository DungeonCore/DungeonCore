package net.l_bulb.dungeoncore.item.setItem;

import net.l_bulb.dungeoncore.item.ItemInterface;

import org.bukkit.Material;

public interface SetItemPartable extends ItemInterface {

  public SetItemInterface getBelongSetItem();

  public SetItemPartsType getItemSetPartsType();

  public Material getMaterial();
}
