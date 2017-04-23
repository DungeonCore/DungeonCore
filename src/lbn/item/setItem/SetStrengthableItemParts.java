package lbn.item.setItem;

import lbn.item.itemInterface.Strengthenable;

import org.bukkit.Material;

public abstract class SetStrengthableItemParts extends SetItemParts implements Strengthenable{

	public SetStrengthableItemParts(SetItemInterface belongSetItem) {
		super(belongSetItem, null, null);
		this.material = getMaterial();
		this.type = getItemSetPartsType();
	}

	abstract public Material getMaterial();

	abstract public SetItemPartsType getItemSetPartsType();
}
