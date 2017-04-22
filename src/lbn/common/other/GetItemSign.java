package lbn.common.other;

import lbn.util.ItemStackUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class GetItemSign extends InHandItemClickSign {

	public GetItemSign(PlayerInteractEvent e) {
		super(e);
	}

	@Override
	protected String getLine2() {
		return "FOR GETTING";
	}

	@Override
	public void doClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
			ItemStackUtil.removeAll(player.getInventory(), signItem.getItem());
			player.setItemInHand(signItem.getItem());
			player.updateInventory();
		} else {
			player.sendMessage(ChatColor.RED + "何も持たないでクリックしてください。");
		}
	}

}
