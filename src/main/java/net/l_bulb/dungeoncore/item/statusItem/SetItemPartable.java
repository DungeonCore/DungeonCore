package net.l_bulb.dungeoncore.item.statusItem;

import org.bukkit.Material;

import net.l_bulb.dungeoncore.item.ItemInterface;

public interface SetItemPartable extends ItemInterface {
  public StatusItemData getBelongSetItem();

  public SetItemPartsType getItemSetPartsType();

  public Material getMaterial();
}
