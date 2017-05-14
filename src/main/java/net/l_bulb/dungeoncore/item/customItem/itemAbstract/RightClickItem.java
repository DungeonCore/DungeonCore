package net.l_bulb.dungeoncore.item.customItem.itemAbstract;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public abstract class RightClickItem extends AbstractItem implements RightClickItemable {
  abstract protected boolean isConsumeWhenUse();

  @Override
  public void excuteOnRightClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    if (excuteOnRightClick2(e) && isConsumeWhenUse()) {
      ItemStackUtil.consumeItemInHand(player);
    }
  }

  abstract protected boolean excuteOnRightClick2(PlayerInteractEvent e);
}
