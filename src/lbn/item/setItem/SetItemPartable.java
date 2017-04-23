package lbn.item.setItem;

import lbn.item.ItemInterface;

import org.bukkit.Material;

public interface SetItemPartable extends ItemInterface{

	public SetItemInterface getBelongSetItem() ;

	public SetItemPartsType getItemSetPartsType();

	public Material getMaterial();
}
