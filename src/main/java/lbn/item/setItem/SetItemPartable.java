package lbn.item.setItem;

import org.bukkit.Material;

import lbn.item.ItemInterface;

public interface SetItemPartable extends ItemInterface {

  public SetItemInterface getBelongSetItem();

  public SetItemPartsType getItemSetPartsType();

  public Material getMaterial();
}
