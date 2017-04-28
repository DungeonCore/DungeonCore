package net.l_bulb.dungeoncore.item.setItem;

import org.bukkit.Material;

import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;

public abstract class SetStrengthableItemParts extends SetItemParts implements Strengthenable {

  public SetStrengthableItemParts(SetItemInterface belongSetItem) {
    super(belongSetItem, null, null);
    this.material = getMaterial();
    this.type = getItemSetPartsType();
  }

  @Override
  abstract public Material getMaterial();

  @Override
  abstract public SetItemPartsType getItemSetPartsType();
}
