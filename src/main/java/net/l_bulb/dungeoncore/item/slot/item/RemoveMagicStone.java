package net.l_bulb.dungeoncore.item.slot.item;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;

public class RemoveMagicStone extends AbstractItem implements RightClickItemable {

  @Override
  public String getItemName() {
    return "吸魂器";
  }

  @Override
  public String getId() {
    return "remove_magicstone_item";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 100;
  }

  @Override
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    return true;
  }

  @Override
  public boolean isConsumeWhenRightClick(PlayerInteractEvent event) {
    return true;
  }

  @Override
  protected Material getMaterial() {
    return Material.REDSTONE_TORCH_ON;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "武器から魔法石を削除します。", "どの魔法石を外すか選択できます。" };
  }

}
