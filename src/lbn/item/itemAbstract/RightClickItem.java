package lbn.item.itemAbstract;

import lbn.item.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;
import lbn.util.ItemStackUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class RightClickItem extends AbstractItem implements RightClickItemable{
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
