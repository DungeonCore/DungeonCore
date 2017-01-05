package lbn.item.itemAbstract;

import lbn.item.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class RightClickItem extends AbstractItem implements RightClickItemable{
	abstract protected boolean isConsumeWhenUse();

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (excuteOnRightClick2(e) && isConsumeWhenUse()) {
			//消費させる
			if (player.getItemInHand().getAmount() == 1) {
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
			} else {
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			}
		}
	}

	abstract protected boolean excuteOnRightClick2(PlayerInteractEvent e);
}
