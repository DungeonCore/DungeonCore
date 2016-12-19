package main.item.key;

import main.dungeon.contents.item.key.AbstractKeyItem;
import main.util.particle.ParticleData;
import main.util.particle.ParticleType;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractTeleportKey extends AbstractKeyItem{
	public AbstractTeleportKey(Location loc) {
		this. loc = loc;
	}

	protected Location loc;

	@Override
	public void onClick(PlayerInteractEvent e, String[] lines, ItemStack item) {
		try {
			Player p = e.getPlayer();
			new ParticleData(ParticleType.portal, 50).run(loc);
			p.playSound(loc, Sound.PORTAL_TRAVEL, 1, 1);
			loc.setDirection(p.getLocation().getDirection());
			//TPする
			p.teleport(loc);

			//アイテムを減らせる
			ItemStack itemInHand = p.getItemInHand();
			if (itemInHand.getAmount() == 1) {
				p.setItemInHand(new ItemStack(Material.AIR));
			} else {
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				p.setItemInHand(itemInHand);
			}
		} catch (NumberFormatException ex) {
			Player player = e.getPlayer();
			if (player.isOp()) {
				player.sendMessage(ChatColor.RED + "座標が不正です");
			}
			return;
		}

	}

	@Override
	public String getLastLine(Player p, String[] params) {
		return "";
	}

}
