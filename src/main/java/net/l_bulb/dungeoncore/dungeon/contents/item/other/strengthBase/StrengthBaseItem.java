package net.l_bulb.dungeoncore.dungeon.contents.item.other.strengthBase;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.ChangeStrengthItemTemplate;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.StrengthChangeItemable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.Message;

public abstract class StrengthBaseItem extends AbstractItem implements StrengthChangeItemable {

  @Override
  public void setStrengthDetail(int level, ItemLoreToken loreToken) {}

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return new ChangeStrengthItemTemplate(getItem(), 1000, 70);
  }

  @Override
  public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
    int nextLevel = event.getNextLevel();
    if (getMaxStrengthCount() == nextLevel) {
      ItemStack newItem = getLastStrengthResultItem().getItem();
      event.setItem(newItem);
    }
  }

  protected abstract ItemInterface getLastStrengthResultItem();

  @Override
  public String[] getDetail() {
    return Message.getMessage("このアイテムを{0}回強化に成功すると, [{1}]になります。", getMaxStrengthCount(), getLastStrengthResultItem().getSimpleName()).split(",");
  }

}
