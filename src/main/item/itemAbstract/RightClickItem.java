package main.item.itemAbstract;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import main.item.AbstractItem;
import main.item.itemInterface.RightClickItemable;

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
