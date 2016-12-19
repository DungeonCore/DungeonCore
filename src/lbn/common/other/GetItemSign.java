package lbn.common.other;

import org.bukkit.event.player.PlayerInteractEvent;

import lbn.item.itemInterface.GettingItemable;

public class GetItemSign extends InHandItemClickSign{

	GettingItemable gettingItem;

	public GetItemSign(PlayerInteractEvent e) {
		super(e);

		if (isSuccess) {
			if (signItem instanceof GettingItemable) {
				gettingItem = (GettingItemable) signItem;
				return;
			} else {
				isSuccess = false;
			}
		}
	}

	@Override
	protected String getLine2() {
		return "FOR GETTING";
	}

	@Override
	public void doClick(PlayerInteractEvent e) {
		gettingItem.onClickForGetting(e, lines);
	}

}
