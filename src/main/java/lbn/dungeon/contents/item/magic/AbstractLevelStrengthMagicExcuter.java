package lbn.dungeon.contents.item.magic;

import org.bukkit.inventory.ItemStack;

import lbn.item.itemInterface.MagicExcuteable;
import lbn.item.system.strength.StrengthOperator;

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
