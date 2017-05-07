package lbn.item.customItem.itemAbstract;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;
import lbn.util.ItemStackUtil;

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
