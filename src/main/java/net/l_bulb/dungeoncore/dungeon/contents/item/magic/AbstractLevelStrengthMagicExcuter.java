package net.l_bulb.dungeoncore.dungeon.contents.item.magic;

import net.l_bulb.dungeoncore.item.itemInterface.MagicExcuteable;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractLevelStrengthMagicExcuter implements MagicExcuteable {

  public AbstractLevelStrengthMagicExcuter(String id, ItemStack item,
      boolean isShowMessage) {
    this.id = id;
    this.item = item;
    this.isShowMessage = isShowMessage;
    this.itemLevel = StrengthOperator.getLevel(item);
  }

  String id;
  ItemStack item;
  boolean isShowMessage;

  int itemLevel;

  public int getItemStrengthLevel() {
    return itemLevel;
  }

  @Override
  public int getCooltimeTick(ItemStack item) {
    return 0;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ItemStack getItem() {
    return item;
  }

  @Override
  public boolean isShowMessageIfUnderCooltime() {
    return isShowMessage;
  }

}
